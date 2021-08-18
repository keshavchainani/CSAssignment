package com.cs.project.domain.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EventConverter {
	
	private static Logger log = LoggerFactory.getLogger(EventConverter.class);
    /**
     * Takes eventDTO start and finish objects and calculates (non)alert event based on elapsed timestamp
     *
     * @param startEvent
     * @param finishEvent
     * @return Event object
     */
    public Event EventDTOToEvent(EventDto startEvent, EventDto finishEvent) {
        Long duration =  finishEvent.getTimestamp() - startEvent.getTimestamp();
        boolean isAlert = duration > 4;
        if(isAlert) {
        log.info("Alert triggered for id: {} total duration is: {}",startEvent.getId(),duration );
        }
        return new Event(startEvent.getId(), duration, startEvent.getType(), startEvent.getHost(), isAlert);
    }
}
