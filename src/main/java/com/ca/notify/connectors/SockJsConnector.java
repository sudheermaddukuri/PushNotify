package com.ca.notify.connectors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.log4j.Logger;
import org.apache.tomcat.websocket.WsWebSocketContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.ca.notify.ApplicationContextProvider;
import com.ca.notify.appconfig.ConfigurationBean;
import com.ca.notify.domain.Notify;
import com.ca.notify.util.StringConstants;
import com.google.gson.Gson;

/**
 * 
 * Connector to the sockjs endpoint as defined by the notificatoin controller.
 *
 */
public class SockJsConnector {

	private static final Logger logger = Logger.getLogger(SockJsConnector.class);

	private static final String tout = "15";
	private static int timeout;

	BlockingQueue<String> blockingQueue;
	WebSocketStompClient stompClient;
	StompSession session;

	static {
		if (!(ConfigurationBean.config.get(StringConstants.SOCKJS_TIMEOUT) == null)
				|| !ConfigurationBean.config.get(StringConstants.SOCKJS_TIMEOUT).equals("")
				|| !ConfigurationBean.config.get(StringConstants.SOCKJS_TIMEOUT).equals("\"")) {
			timeout = Integer.parseInt(ConfigurationBean.config.get(StringConstants.SOCKJS_TIMEOUT));
		} else {
			timeout = Integer.parseInt(tout);
		}
	}

	@Deprecated
	/**
	 * Send message like you are pushing it from an external web socket.
	 * 
	 */
	public String sendMessage(String msg) {

		List<Transport> transports = new ArrayList<Transport>();

		if (ConfigurationBean.config.get(StringConstants.SOCK_JS_ENDPOINT).contains("https"))
			transports.add(new WebSocketTransport(getSSLStompClient()));
		else
			transports.add(new WebSocketTransport(new StandardWebSocketClient()));

		transports.add(new RestTemplateXhrTransport());

		blockingQueue = new LinkedBlockingQueue<String>();
		stompClient = new WebSocketStompClient(new SockJsClient(transports));

		try {
			logger.info("Start creating stomp session to:"
					+ ConfigurationBean.config.get(StringConstants.SOCK_JS_ENDPOINT) + "With timeout : " + timeout);
			session = stompClient.connect(ConfigurationBean.config.get(StringConstants.SOCK_JS_ENDPOINT),
					new StompSessionHandlerAdapter() {
					}).get(timeout, TimeUnit.SECONDS);
			session.send("/app/capns", msg.getBytes());
		} catch (InterruptedException e) {
			logger.error(e);
		} catch (ExecutionException e) {
			logger.error(e);
		} catch (TimeoutException e) {
			logger.error(e);
		} finally {
			if (session != null)
				session.disconnect();
		}

		return "";
	}

	/**
	 * Send message using a simple messaging template to put it directly to the
	 * channel. Grab the <code>SimpMessagingTemplate</code> which is initialized
	 * in the <code>CaNotificationController</code>
	 * 
	 */
	public String sendMessageWithinVm(String msg) {

		ApplicationContextProvider pvd = new ApplicationContextProvider();
		SimpMessagingTemplate mT = pvd.getApplicationContext().getBean(SimpMessagingTemplate.class);

		Notify nf = new Gson().fromJson(msg, Notify.class);

		if (!nf.getId().equals("") && !nf.getCategory().toString().equals(""))
			mT.convertAndSend("/topic/public/" + nf.getCategory().toString() + "/" + nf.getId(), nf);
		else
			mT.convertAndSend("/topic/public", nf);

		return "";
	}

	class DefaultStompFrameHandler implements StompFrameHandler {

		public Type getPayloadType(StompHeaders stompHeaders) {
			return byte[].class;
		}

		public void handleFrame(StompHeaders stompHeaders, Object o) {
			blockingQueue.offer(new String((byte[]) o));
		}
	}

	/**
	 * Creates and return the stomp ssl client.
	 * 
	 */
	private StandardWebSocketClient getSSLStompClient() {
		StandardWebSocketClient wsClient = null;
		InputStream keystoreLocation = null;
		InputStream truststoreLocation = null;

		try {

			String keystoreType = "JKS";

			try {
				keystoreLocation = new FileInputStream(ConfigurationBean.config.get(StringConstants.KEYSTORE));
			} catch (FileNotFoundException e) {
				keystoreLocation = this.getClass().getResourceAsStream(
						"/" + ConfigurationBean.config.get(StringConstants.KEYSTORE).split(":")[1]);
			} finally {

			}

			char[] keystorePassword = ConfigurationBean.config.get(StringConstants.KEYSTORE_PWD).toCharArray();
			char[] keyPassword = ConfigurationBean.config.get(StringConstants.KEYSTORE_PWD).toCharArray();

			KeyStore keystore = KeyStore.getInstance(keystoreType);
			keystore.load(keystoreLocation, keystorePassword);
			KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmfactory.init(keystore, keyPassword);

			try {
				truststoreLocation = new FileInputStream(ConfigurationBean.config.get(StringConstants.TRUSTSTORE));
			} catch (FileNotFoundException e) {
				truststoreLocation = this.getClass().getResourceAsStream(
						"/" + ConfigurationBean.config.get(StringConstants.TRUSTSTORE).split(":")[1]);
			} finally {

			}

			char[] truststorePassword = ConfigurationBean.config.get(StringConstants.TRUSTSTORE_PWD).toCharArray();
			String truststoreType = "JKS";

			KeyStore truststore = KeyStore.getInstance(truststoreType);
			truststore.load(truststoreLocation, truststorePassword);
			TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmfactory.init(truststore);

			KeyManager[] keymanagers = kmfactory.getKeyManagers();
			TrustManager[] trustmanagers = tmfactory.getTrustManagers();

			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(keymanagers, trustmanagers, new SecureRandom());
			SSLContext.setDefault(sslContext);

			wsClient = new StandardWebSocketClient();
			Map<String, Object> props = new HashMap<String, Object>();
			props.put(WsWebSocketContainer.SSL_CONTEXT_PROPERTY, sslContext);
			wsClient.setUserProperties(props);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (keystoreLocation != null) {
				try {
					keystoreLocation.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}

			if (truststoreLocation != null) {
				try {
					truststoreLocation.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return wsClient;
	}

}
