package org.openpaas.servicebroker.kubernetes.service.impl;

import java.util.Map;

import org.openpaas.servicebroker.kubernetes.exception.KubernetesServiceException;
import org.openpaas.servicebroker.kubernetes.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;

@Service
public class TemplateServiceImpl implements TemplateService {
	
	private static final Logger logger = LoggerFactory.getLogger(TemplateServiceImpl.class);

    private JdbcTemplate jdbcTemplate;

    private Configuration configuration;

	@Lazy
    @Autowired
	public TemplateServiceImpl(JdbcTemplate jdbcTemplate, Configuration configuration) {
		this.jdbcTemplate = jdbcTemplate;
		this.configuration = configuration;

		logger.info( "JDBC.JdbcTemplate : {}", this.jdbcTemplate.toString());
		logger.info( "freemaker.Configuration : {}", this.configuration.toString());
	}

	@Override
	public boolean execute(String templateName, Map<String, Object> model) throws KubernetesServiceException {
		try {
			String sql = FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate(templateName), model).replaceAll("\\n", " ");
			logger.info("original sql {}",sql);
			String[] commands = sql.split(";");
			for(String command : commands){
				logger.info("executing {}",command);
				if(command.trim().length() > 0){
					jdbcTemplate.execute(command.trim());
				}
			}
		} catch (Exception e) {
			logger.error( "Occured unexpected exception...", e );
			return false;
		}
		return true;
	}
	
	@Override
	public String convert(String templateName, Map<String, Object> model) throws KubernetesServiceException {
		String yml;
		try {
			yml = FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate(templateName), model);
			logger.info("original yml {}",yml);
		} catch (Exception e) {
			logger.error( "Occured unexpected exception...", e );
			return null;
		}
		return yml;
	}
}
