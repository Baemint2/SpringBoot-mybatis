package com.moz1mozi.mybatis.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

import java.text.SimpleDateFormat;

public class MyLogbackLayout extends LayoutBase<ILoggingEvent> {

    @Override
    public String doLayout(ILoggingEvent event) {

        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(new SimpleDateFormat("HH:mm:ss.SSS").format(event.getTimeStamp()));
        builder.append("]");
        builder.append(String.format("%6s", event.getLevel()));
        String logger = event.getLoggerName();
        builder.append(" | ").append(logger).append(" | ");
        if(logger.equals("jdbc.sqltiming")) {
            builder.append(CoreConstants.LINE_SEPARATOR).append("    ");
        }
        builder.append(event.getFormattedMessage());
        builder.append(CoreConstants.LINE_SEPARATOR);

        return builder.toString();

    }

}