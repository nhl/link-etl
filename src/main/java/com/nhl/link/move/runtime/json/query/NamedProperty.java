package com.nhl.link.move.runtime.json.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.Collections;
import java.util.List;

public class NamedProperty implements JsonQuery {

    private JsonQuery clientQuery;
    private String propertyName;

    public NamedProperty(JsonQuery clientQuery, String propertyName) {
        this.clientQuery = clientQuery;
        this.propertyName = propertyName;
    }

    @Override
    public List<JsonNode> execute(JsonNode rootNode) {
        return execute(rootNode, rootNode);
    }

    @Override
    public List<JsonNode> execute(JsonNode rootNode, JsonNode currentNode) {

        if (currentNode == null) {
            return Collections.emptyList();
        }

        JsonNode node;
        if (currentNode instanceof ArrayNode) {
            Integer index;
            try {
                 index = Integer.valueOf(propertyName);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Illegal index: " + propertyName);
            }
            if (index < 0) {
                throw new RuntimeException("Illegal index: " + propertyName);
            }
            node = currentNode.get(index);

        } else {
            node = currentNode.get(propertyName);
        }

        if (node == null) {
            return Collections.emptyList();
        }

        return clientQuery == null?
                Collections.singletonList(node) : clientQuery.execute(rootNode, node);
    }
}