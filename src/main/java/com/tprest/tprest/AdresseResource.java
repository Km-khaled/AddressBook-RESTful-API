package com.tprest.tprest;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.ResultSet;
import javax.ws.rs.*;
import java.util.*;

@Path("carnets/{nom_carnet}/adresses")

public class AdresseResource {

	private Connection connection;

	public AdresseResource() throws SQLException {
		try {
			connection = SingletonConnection.getConnection();
		} catch (SQLException e) {

			throw new SQLException("Failed to initialize CarnetResource", e);
		}
	}

	private int getCarnetIdByName(String nomCarnet) {
		int id_carnet = -1;


		try {
			String sql = "SELECT id FROM crnt_org WHERE nom = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, nomCarnet);

			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				id_carnet = resultSet.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id_carnet;
	}


	
	
	
	@POST
	@Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
	public Response enregistrerAdresse(@PathParam("nom_carnet") String nomCarnet,String adresseData,@Context HttpHeaders request) {
	    int idCarnet = getCarnetIdByName(nomCarnet);

	    String rue = null, ville = null, nom = null;

	    if (adresseData.startsWith("rue:")) {
	        String[] parts = adresseData.split(",");

	        if (parts.length < 3) {
	            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid data format").build();
	        }

	        rue = parts[0].split(":")[1].trim();
	        ville = parts[1].split(":")[1].trim();
	        nom = parts[2].split(":")[1].trim();


	    } else {

	    	try {
	            JSONObject jsonData = new JSONObject(adresseData);
	            rue = jsonData.getString("rue");
	            ville = jsonData.getString("ville");
	            nom = jsonData.getJSONObject("personne").getString("nom");
	        } catch (JSONException e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON data format").build();
	        }
	    }

	    try {
	        Adresses adresse = new Adresses(rue, ville, new Personnes(nom));

	        String sql = "INSERT INTO carnet (nom, rue, ville, id_ch) VALUES (?, ?, ?, ?)";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setString(1, nom);
	        statement.setString(2, rue);
	        statement.setString(3, ville);
	        statement.setInt(4, idCarnet);

	        int rowsInserted = statement.executeUpdate();

	        if (rowsInserted > 0) {
	            return Response.status(Response.Status.CREATED).entity("Adresse enregistrée avec succès").build();
	        } else {
	            return Response.status(Response.Status.NOT_FOUND).entity("Carnet non trouvé").build();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                .entity("Erreur lors de l'enregistrement de l'adresse").build();
	    }
	}

	
	
	@DELETE
	@Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
	public Response effacerAdresse(@PathParam("nom_carnet") String nomCarnet, String input,@Context HttpHeaders request) {

	    int idCarnet = getCarnetIdByName(nomCarnet);

	    String nomPersonne = null;
	    try {
	    	
	    	 if (input.startsWith("nom:")) { 
		                nomPersonne = input.substring(4).trim();
		            
		        }
	    	
	    	
	    	 else {
	            // Parse JSON data
	            JSONObject jsonData = new JSONObject(input);
	            nomPersonne = jsonData.getString("nom");
	        }

	        // delete it from database
	        String sql = "DELETE FROM carnet WHERE nom = ? and id_ch=?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setString(1, nomPersonne);
	        statement.setInt(2, idCarnet);

	        int rowsDeleted = statement.executeUpdate();

	        if (rowsDeleted > 0) {
	            String message = "Adresse supprimée avec succès";
	            return Response.status(Response.Status.OK).entity(message).build();
	        } else {
	            String errorMessage = "Adresse non trouvée dans le carnet spécifié";
	            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
	        }
	    } catch (JSONException | SQLException e) {
	        e.printStackTrace();
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                .entity("Erreur lors de l'enregistrement de l'adresse").build();
	    }
	}


	@GET
	@Path("{nom_Personne}")
	@Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
	public Response chercherAdresse(@PathParam("nom_carnet") String nomCarnet,  
	                                 @PathParam("nom_Personne") String nomPersonne, 
	                                 @Context HttpHeaders request) {
	    int idCarnet = getCarnetIdByName(nomCarnet);
	    List<Adresses> adressesList = new ArrayList<>();

	    try {
	        String sql = "SELECT rue, ville FROM carnet WHERE nom = ? AND id_ch=?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setString(1, nomPersonne);
	        statement.setInt(2, idCarnet);

	        ResultSet resultSet = statement.executeQuery();

	        while (resultSet.next()) {
	            Personnes personne = new Personnes(nomPersonne);
	            Adresses adresse = new Adresses(resultSet.getString("rue"), resultSet.getString("ville"), personne);
	            adressesList.add(adresse);
	        }

	        if (!adressesList.isEmpty()) {
	            List<MediaType> acceptableMediaTypes = request.getAcceptableMediaTypes();

	            if (acceptableMediaTypes.contains(MediaType.APPLICATION_JSON_TYPE)) {
	                JSONArray jsonAddresses = new JSONArray();
	                for (Adresses adresse : adressesList) {
	                    jsonAddresses.put(adresse.toJSON());
	                }
	                return Response.status(Response.Status.OK).entity(jsonAddresses.toString()).type(MediaType.APPLICATION_JSON).build();
	            } else if (acceptableMediaTypes.contains(MediaType.TEXT_PLAIN_TYPE)) {
	                StringBuilder plainText = new StringBuilder();
	                for (Adresses adresse : adressesList) {
	                    plainText.append(adresse.toString()).append("\n");
	                }
	                return Response.status(Response.Status.OK).entity(plainText.toString()).type(MediaType.TEXT_PLAIN).build();
	            } else {
	                return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Aucun type de média acceptable trouvé.").build();
	            }
	        } else {
	            return Response.status(Response.Status.NOT_FOUND).entity("Aucune adresse trouvée pour la personne spécifiée dans le carnet.").build();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                .entity("Erreur lors de la recherche des adresses").build();
	    }
	}



	@GET
	@Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
	public Response listerAdresses(@PathParam("nom_carnet") String nomCarnet, @Context HttpHeaders request) {
	    int idCarnet = getCarnetIdByName(nomCarnet);
	    List<Adresses> adresses = new ArrayList<>();

	    try {
	        String sql = "SELECT nom, rue, ville FROM carnet WHERE id_ch=?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setInt(1, idCarnet);
	        ResultSet resultSet = statement.executeQuery();

	        while (resultSet.next()) {
	            String nomPersonne = resultSet.getString("nom");
	            String rue = resultSet.getString("rue");
	            String ville = resultSet.getString("ville");
	            Personnes personne = new Personnes(nomPersonne);
	            Adresses adresse = new Adresses(rue, ville, personne);
	            adresses.add(adresse);
	        }
	        
            List<MediaType> acceptableMediaTypes = request.getAcceptableMediaTypes();


	        if (acceptableMediaTypes.contains(MediaType.APPLICATION_JSON_TYPE)) {
	            JSONArray jsonArray = new JSONArray();
	            for (Adresses adresse : adresses) {
	                jsonArray.put(adresse.toJSON());
	            }
	            return Response.status(Response.Status.OK).entity(jsonArray.toString()).type(MediaType.APPLICATION_JSON).build();
	        } else {

	        	StringBuilder responseText = new StringBuilder();
				for (Adresses adresse : adresses) {
					responseText.append(adresse.toString()).append("\n");
				}

				return Response.status(Response.Status.OK).entity(responseText.toString()).build();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                .entity("Erreur lors de la recherche des adresses").build();
	    }
	}

}