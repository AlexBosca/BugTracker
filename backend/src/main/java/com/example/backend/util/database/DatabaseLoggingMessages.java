package com.example.backend.util.database;

public class DatabaseLoggingMessages {

    public static final String ENTITY_SAVED = "Entity saved: {}";
    public static final String ENTITY_SAVE_ERROR = "Error occurred while saving entity: {}";
    public static final String ENTITIES_FETCHED = "Entities fetched successfully";
    public static final String ENTITIES_FILTERED_FETCHED = "Entities fetched successfully using filter: {}";
    public static final String ENTITY_FETCHED = "Entity fetched successfully";
    public static final String ENTITY_FETCH_ERROR = "Error occurred while fetching entity: {}";
    public static final String ENTITY_NOT_FOUND = "Entity not found: {}";
    public static final String ENTITY_UPDATED = "Entity updated: {}";
    public static final String ENTITY_UPDATE_ERROR = "Error occurred while updating entity: {}";
    public static final String ENTITY_DELETED = "Entity deleted: {}";
    public static final String ENTITY_DELETE_ERROR = "Error occurred while deleting entity: {}";
    public static final String ENTITY_EXISTS = "Entity exists: {}";
    public static final String ENTITY_NOT_EXISTS = "Entity doesn't exist: {}";
    public static final String ENTITY_EXISTS_ERROR = "Error occurred while checking if entity exists: {}";
    
    private DatabaseLoggingMessages() { }
}
