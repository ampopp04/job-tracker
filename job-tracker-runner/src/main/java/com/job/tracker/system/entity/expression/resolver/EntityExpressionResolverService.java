package com.job.tracker.system.entity.expression.resolver;

import com.job.tracker.system.entity.expression.EntityExpression;
import com.job.tracker.system.entity.expression.assertion.AssertUtils;
import com.system.db.entity.Entity;
import com.system.db.entity.base.BaseEntity;
import com.system.inversion.component.InversionComponent;
import com.system.logging.exception.util.ExceptionUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static com.system.util.string.StringUtils.decapitalize;
import static com.system.ws.entity.upload.util.EntityPropertyUtils.getEntityBeanPropertyNameValueMap;

/**
 * The <class>EntityExpressionResolverService</class> will
 * take an expression and resolve it against a specific entity instance.
 * <p>
 * This service actually performs the work of executing the expression
 * template using a templating engine to resolve and return the calculated expression
 * result.
 *
 * @author Andrew Popp
 */
@InversionComponent
public class EntityExpressionResolverService {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private Configuration freeMarkerConfiguration;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpressionResolverService() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Resolver Methods                                                  //////////
    //////////////////////////////////////////////////////////////////////

    public Object resolveSpelExpression(EntityExpression entityExpression, Entity entity) {
        try {
            ExpressionParser expressionParser = new SpelExpressionParser();
            Expression expression = expressionParser.parseExpression(entityExpression.getEntityExpressionExpanded());
            StandardEvaluationContext context = new StandardEvaluationContext(entity);
            return expression.getValue(context);
        } catch (Exception e) {
            ExceptionUtils.throwSystemException("Error Parsing Object Operation Expression", e);
        }

        return null;
    }

    public String resolve(String expressionName, String expression, Entity entity) {
        AssertUtils.assertNotNull(expression, "Expression provided was empty for entity [" + entity.toString() + "] and expression name [" + expressionName + "]");
        AssertUtils.assertNotEmpty(expressionName, "Expression name provided was empty for entity [" + entity.toString() + "] and expression name [" + expressionName + "]");

        Template template = null;

        try {
            template = new Template(expressionName, new StringReader(expression), getFreeMarkerConfiguration());
        } catch (IOException e) {
            throw new RuntimeException("Error generating formatted string from config [" + expressionName + "].  Error: [" + e.getMessage() + "]", e);
        }

        String result;

        Map<String, Object> root = new HashMap<>();
        createTemplateDataModel(root, entity);

        try (Writer out = new StringWriter();) {
            template.process(root, out);
            result = out.toString();
        } catch (Throwable e) {
            throw new RuntimeException("Error generating formatted string from config [" + expressionName + "].  Error: [" + e.getMessage() + "]", e);
        }

        return result;
    }

    private void createTemplateDataModel(Map<String, Object> root, Entity rootEntity) {
        root.put(decapitalize(rootEntity.getClass().getSimpleName()), rootEntity);
        Map<String, Object> propertyNameValueMap = getEntityBeanPropertyNameValueMap((BaseEntity) rootEntity);
        root.putAll(propertyNameValueMap);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public Configuration getFreeMarkerConfiguration() {
        return freeMarkerConfiguration;
    }

    public void setFreeMarkerConfiguration(Configuration freeMarkerConfiguration) {
        this.freeMarkerConfiguration = freeMarkerConfiguration;
    }
}
