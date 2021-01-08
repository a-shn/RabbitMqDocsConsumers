package ru.itis.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import ru.itis.dtos.DocsData;
import ru.itis.services.PdfMaker;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class WorkAdmissionDoc {
    private static final String EXCHANGE_NAME = "create_docs_exchange";
    private static final String BINDING_KEY = "#.work_admission.#";
    private static final String TEMPLATE_NAME = "work_admission.ftl";

    public static void main(String[] args) {
        PdfMaker pdfMaker = new PdfMaker();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            String queue = channel.queueDeclare().getQueue();
            channel.queueBind(queue, EXCHANGE_NAME, BINDING_KEY);
            System.out.println("[*] Waiting for messages.");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                DocsData docsData = objectMapper.readValue(message, DocsData.class);
                System.out.println("[x] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
                pdfMaker.makePdf(TEMPLATE_NAME, docsData.getData());
            };
            channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
