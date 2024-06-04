package com.tprest.tprest;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.core.HttpHeaders;


@Path("carnets")
public class CarnetResource {

	  private Connection connection;

	    public CarnetResource() throws SQLException {
	        try {
	            connection = SingletonConnection.getConnection();
	        } catch (SQLException e) {
	            throw new SQLException("Failed to initialize CarnetResource", e);
	        }
	    }
	    @POST
	    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	    public Response creerCarnet(String input) {
	        try {
	            String nom;
	            if (input.startsWith("nom:")) {
	                nom = input.substring(4).trim();
	            } else {
	                JSONObject json = new JSONObject(input);
	                nom = json.getString("nom");
	                }

	            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO crnt_org (nom) VALUES (?)")) {
	                statement.setString(1, nom);
	                int rowsInserted = statement.executeUpdate();

	                if (rowsInserted > 0) {
	                    return Response.status(Response.Status.CREATED)
	                            .entity("Carnet '" + nom + "' créé avec succès").build();
	                } else {
	                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                            .entity("Erreur lors de la création du carnet").build();
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("Erreur lors de la création du carnet").build();
	        }
	    }

	    
	    
	    
	    @GET
	    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	    public Response listerCarnets(@Context HttpHeaders headers) {
	        try (PreparedStatement statement = connection.prepareStatement("SELECT nom FROM crnt_org");
	             ResultSet resultSet = statement.executeQuery()) {

	            List<MediaType> acceptedMediaTypes = headers.getAcceptableMediaTypes();

	            if (acceptedMediaTypes.contains(MediaType.APPLICATION_JSON_TYPE)) {

	            	JSONArray jsonArray = new JSONArray();
	                while (resultSet.next()) {
	                    String nomCarnet = resultSet.getString("nom");
	                    jsonArray.put("nom: "+nomCarnet);
	                }
	                return Response.status(Response.Status.OK).entity(jsonArray.toString()).type(MediaType.APPLICATION_JSON).build();
	            } else {

	            	StringBuilder sb = new StringBuilder();
	                while (resultSet.next()) {
	                    String nomCarnet = resultSet.getString("nom");
	                    sb.append(nomCarnet).append("\n");
	                }
	                return Response.status(Response.Status.OK).entity(sb.toString()).type(MediaType.TEXT_PLAIN).build();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("Erreur lors de la recherche des carnets").build();
	        }
	    }

	    @DELETE
	    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	    public Response supprimerCarnet(String input) {
	        String nom = null;
	        try {

	        	JSONObject json = new JSONObject(input);
	            nom = json.getString("nom");
	        } catch (JSONException e) {

	        	if (input.startsWith("nom:")) {
	                nom = input.substring(4).trim();
	            }
	        }

	        if (nom == null) {

	        	return Response.status(Response.Status.BAD_REQUEST).entity("Le nom du carnet est manquant").build();
	        }

	        try (PreparedStatement statement = connection.prepareStatement("SELECT id FROM crnt_org WHERE nom=?")) {
	            statement.setString(1, nom);
	            ResultSet resultSet = statement.executeQuery();

	            if (resultSet.next()) {
	                int id = resultSet.getInt("id");

	                try (PreparedStatement statementDeleteCarnet = connection
	                        .prepareStatement("DELETE FROM carnet WHERE id_ch = ?")) {
	                    statementDeleteCarnet.setInt(1, id);
	                    statementDeleteCarnet.executeUpdate();
	                }

	                try (PreparedStatement statementDeleteCarnetOrg = connection
	                        .prepareStatement("DELETE FROM crnt_org WHERE nom = ?")) {
	                    statementDeleteCarnetOrg.setString(1, nom);
	                    statementDeleteCarnetOrg.executeUpdate();
	                }

	                return Response.status(Response.Status.OK).entity("Carnet '" + nom + "' supprimé avec succès").build();
	            } else {
	                return Response.status(Response.Status.NOT_FOUND).entity("Carnet '" + nom + "' non trouvé").build();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("Erreur lors de la suppression du carnet").build();
	        }
	    }

}