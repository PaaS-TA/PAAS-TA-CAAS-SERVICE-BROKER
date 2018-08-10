package org.openpaas.servicebroker.kubernetes.service.impl;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The type Ssh service.
 */
@Service
public class SshService {
	
    public static final String RESULT_STATUS_FAIL = "FAIL";

    public static final String STRICT_HOST_KEY_CHECKING_STRING = "StrictHostKeyChecking";

    public static final String STRICT_HOST_KEY_CHECKING_VALUE = "no";

    public static final int INPUT_STREAM_READ_SIZE = 1024;

    private static final Logger LOGGER = LoggerFactory.getLogger(SshService.class);
    /**
     * The caas master host url.
     */
    private final String caasClusterUrl;
    /**
     * The caas master host port.
     */
    private final int caasClusterPort;
    /**
     * The caas master host name.
     */
    private final String caasClusterUserName;
    /**
     * The caas master host password.
     */
    private final String caasClusterUserPassword;


    /**
     * Instantiates a new Ssh service.
     *
     * @param propertyService the property service
     */
    public SshService(PropertyService propertyService) {
        this.caasClusterUrl = propertyService.getCaasClusterUrl();
        this.caasClusterPort = propertyService.getCaasClusterPort();
        this.caasClusterUserName = propertyService.getCaasClusterUserName();
        this.caasClusterUserPassword = propertyService.getCaasClusterUserPassword();
    }


    /**
     * Execute ssh string.
     *
     * @param command the command
     * @return the string
     */
    String executeSsh(String command) {
        Session session = null;
        String result = RESULT_STATUS_FAIL;

        try {
            session = getJSchSession();

            if (session != null) {
                result = getResult(session, command);
            }
        } catch (Exception e) {
            LOGGER.error("## executeSsh :: exception :: {}", e);
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }

        return result;
    }


    /**
     * Gets j sch session.
     *
     * @return the j sch session
     */
    private Session getJSchSession() {
        Session session;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(caasClusterUserName, caasClusterUrl, caasClusterPort);

            Properties config = new Properties();
            config.put(STRICT_HOST_KEY_CHECKING_STRING, STRICT_HOST_KEY_CHECKING_VALUE);

            session.setPassword(caasClusterUserPassword);
            session.setConfig(config);
            session.connect();
        } catch (JSchException e) {
            session = null;
            LOGGER.error("## getJSchSession :: JSchException :: {}", e);
        }

        return session;
    }


    /**
     * Gets result.
     *
     * @param session the session
     * @param command the command
     * @return the result
     */
    private String getResult(Session session, String command) {
        StringBuilder result = new StringBuilder();
        Channel channel = null;
        InputStream in;

        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            in = channel.getInputStream();
            channel.connect();

            byte[] bytes = new byte[INPUT_STREAM_READ_SIZE];

            while (true) {
                while (in.available() > 0) {
                    int i = in.read(bytes, 0, INPUT_STREAM_READ_SIZE);
                    if (i < 0) break;
                    result.append(new String(bytes, 0, i));
                }
                if (channel.isClosed()) break;
                try {Thread.sleep(1000);} catch (Exception ignored) {}
            }
        } catch (IOException | JSchException e) {
            LOGGER.error("## getResult :: IOException | JSchException :: {}", e);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }

        return result.toString();
    }
}
