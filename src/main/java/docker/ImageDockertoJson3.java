package docker;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class ImageDockertoJson3 {
	static String imageName;
	static String lastPushed;
	static String imageShortDescription;
	static String dockerPullCommand;
	static String imageVesion;
	static String imageLinkGit;
	static String dockerRunCommand;
	static String linkWikipedia;
	static String linkDBpedia;
	static String dockerVersionSupported;
	static Document doc, doc2;
	static Elements eCodes;
	static String rdf = "";
	public static int getResponseCode(String urlString) throws MalformedURLException, IOException {
		URL u = new URL(urlString);
		HttpURLConnection huc = (HttpURLConnection) u.openConnection();
		huc.setRequestMethod("GET");
		huc.connect();
		return huc.getResponseCode();
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	private static String getUrlContents(String theUrl) {
		StringBuilder content = new StringBuilder();

		// many of these calls can throw exceptions, so i've just
		// wrapped them all in one try/catch statement.
		try {
			// create a url object
			URL url = new URL(theUrl);

			// create a urlconnection object
			URLConnection urlConnection = url.openConnection();

			// wrap the urlconnection in a bufferedreader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			String line;

			// read from the urlconnection via the bufferedreader
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}

	public static void main(String[] args) throws Throwable {
		Elements newsHeadlines, newsHeadlines2;
		Element newsHeadline;
		HttpResponse<JsonNode> response;
		int i = 0;
		int index = 0;
		String url;
		ArrayList listEcodes= new ArrayList();
		String tagName="";
		org.jsoup.nodes.Attributes att;
		String[] someArray;
		int nbTag=0;
		int nbImage=0;
		int nbFROM=0;
		int nbWORKDIR=0;
		int nbADD=0;
		int nbEXPOSE=0;     
		int nbVOLUME=0;
		int nbCMD=0;
		int nbENTRYPOINT=0;
		int nbENV=0;
		int nbUSER=0;
		// ********** block column headers line CSV file ***** //

		String sFileMeta = "C:/docker/meta.rdf";// "/Users/BENAYED-PC/Desktop/docker/Test.csv";/////"Users/BENAYED-PC/Bureau/docker/Test.csv"

		FileWriter writer = new FileWriter(sFileMeta);

		String dockerfiles = "";
		JSONArray imagesarray = new JSONArray();
		JSONObject images = new JSONObject();

		
		rdf = "<?xml version=\"1.0\"?>\n" + "<!DOCTYPE rdf:RDF ["
				+ "				<!ENTITY owl \"http://www.w3.org/2002/07/owl#\" >\n"
				+ "				<!ENTITY nerd \"http://nerd.eurecom.fr/ontology#\" >\n"
				+ "				<!ENTITY xsd \"http://www.w3.org/2001/XMLSchema#\" >\n"
				+ "				<!ENTITY rdfs \"http://www.w3.org/2000/01/rdf-schema#\" >\n"
				+ "				<!ENTITY jrcn \"http://mlode.nlp2rdf.org/jrc-names/\" >\n"
				+ "				<!ENTITY rdf \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" >\n"
				+ "			]>\n" + "			<rdf:RDF xmlns=\"http://www.w3.org/2002/07/owl#\"\n"
				+ "				 xml:base=\"http://www.w3.org/2002/07/owl\"\n"
				+ "				 xmlns:nerd=\"http://nerd.eurecom.fr/ontology#\"\n"
				+ "				 xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\n"
				+ "				 xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\n"
				+ "				 xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"\n"
				+ "				 xmlns:jrcn=\"http://mlode.nlp2rdf.org/jrc-names/\"\n"
				// *** Used ***
				+ "				 xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n"
				+ "				 xmlns:VirtualComponent=\"http://TSE.com/VirtualComponent/#\">\n"; // define
																						// container
		writer.append(rdf);

		
		
		
		 for (i = 9; i <= 9; i++) { doc =
		 Jsoup.connect("https://hub.docker.com/explore/?page=" + i).get();
		 newsHeadlines = doc.select(".RepositoryListItem__flexible___3R0Sg");
		 for (Element element : newsHeadlines) { String attr =
		 element.select("a").first().attr("href");
		
		//String attr = "/_/hello-world/";
		url = "https://hub.docker.com" + attr;
		
		
		
		

		doc = Jsoup.connect(url).get();
		newsHeadlines2 = doc
				.select("div.Card__block___1G9Iy div.Markdown__markdown___527C8 li a[href*=https://github.com/] ");

		
			for (Element langelement : newsHeadlines2) {
				eCodes = langelement.select("code");
				//System.out.println(eCodes.text());
				for (Element eCode : eCodes) {
				listEcodes.add(eCode.text().toString());
				}
			}
		
		// 1. image name
		newsHeadline = doc.select("h2.RepositoryPageWrapper__repoTitle___3r12T a").get(0);
		try {
			imageName = newsHeadline.text();
			System.out.println(imageName);
		} catch (Exception e) {
			imageName = null;
		}
		
		writer.append("<rdf:Description rdf:about=\"" + url + "\">\n");
		writer.append("<VirtualComponent:imageName>" + imageName + "</VirtualComponent:imageName>\n");
		try{
		JSONObject json1 = readJsonFromUrl("https://api.microbadger.com/v1/images/" + imageName);
		String chaine = json1.get("Versions").toString().substring(1, json1.get("Versions").toString().length() - 1);
		
		
		JSONArray allImages = new JSONArray("["+chaine+"]");
		
		for (int j = 0; j < allImages.length(); j++) {
			JSONObject imageObj1 = new JSONObject(allImages.get(j).toString());
			//System.out.println(allImages.get(j).toString());
			JSONArray tags = new JSONArray(imageObj1.get("Tags").toString());
			
			for (int l = 0; l < tags.length(); l++) {
			
			JSONObject tag = new JSONObject(tags.get(l).toString());
			//System.out.println(tags.get(l).toString());
			tagName=tag.get("tag").toString();
			if(listEcodes.contains(tagName)){
				writer.append("<VirtualComponent:tag tag=\""+tag.get("tag")+"\">\n");
			
			
				writer.append("<VirtualComponent:layerCount>" + imageObj1.get("LayerCount") + "</VirtualComponent:layerCount>\n");
				writer.append("<VirtualComponent:downloadSize>" + imageObj1.get("DownloadSize") + "</VirtualComponent:downloadSize>\n");
				writer.append("<VirtualComponent:dockerId_SHA>" + imageObj1.get("SHA") + "</VirtualComponent:dockerId_SHA>\n");

			//System.out.println(versionsObj.toString());
		
			try {
				someArray=null;
				for (Element langelement : newsHeadlines2) {
					att = langelement.select("a[href*=https://github.com/]").get(0).attributes();
					eCodes = langelement.select("code");
					//System.out.println(eCodes.text());
					for (Element eCode : eCodes) {
						
						if(tagName.equals(eCode.text())){
							nbTag++;
						//System.out.println(tagName+"------->"+eCode.text());
					
					 // fin for "all codes"
					String s1 = att.get("href");
					String s2 = s1.replaceAll("/blob", "");
					String s3 = s2.replaceAll("github.com", "raw.github.com");
					String output = getUrlContents(s3);
					output = output.replaceAll("\n", "@");
					someArray = output.split("@");
					
					//System.out.println(output);
					
					for (int ligne = 0; ligne < someArray.length; ligne++) {
						someArray[ligne] = someArray[ligne].replaceAll("@", "");
						//System.out.println(someArray[ligne]);
						String[] tab = someArray[ligne].split(" ");
						String dockerFileCommand=tab[0].replaceAll(" ", "");
						String dockerFileCommandContent="";
					} // end for dockerfile
						
					
					if(output.contains("FROM"))
						for (int ligne = 0; ligne < someArray.length; ligne++) {
							someArray[ligne] = someArray[ligne].replaceAll("@", "");
							//System.out.println(someArray[ligne]);
							String[] tab = someArray[ligne].split(" ");
							String dockerFileCommand=tab[0].replaceAll(" ", "");
							String dockerFileCommandContent="";
						
						 if(dockerFileCommand.equals("FROM")){
							 nbFROM++;
							 dockerFileCommandContent=someArray[ligne].substring(dockerFileCommand.length()+1, someArray[ligne].length());
							 writer.append("<VirtualComponent:basicImage>" + dockerFileCommandContent + "</VirtualComponent:basicImage>\n");
								break;
						 }
						} // end for dockerfile
						 if(!output.contains("FROM"))
							 writer.append("<VirtualComponent:basicImage></VirtualComponent:basicImage>\n");

						 
							 
						 
						 
						 
						 if(output.contains("WORKDIR"))
								for (int ligne = 0; ligne < someArray.length; ligne++) {
									someArray[ligne] = someArray[ligne].replaceAll("@", "");
									//System.out.println(someArray[ligne]);
									String[] tab = someArray[ligne].split(" ");
									String dockerFileCommand=tab[0].replaceAll(" ", "");
									String dockerFileCommandContent="";
								
								 if(dockerFileCommand.equals("WORKDIR")){
									 nbWORKDIR++;
									 dockerFileCommandContent=someArray[ligne].substring(dockerFileCommand.length()+1, someArray[ligne].length());
									 writer.append("<VirtualComponent:principalFolder>" + dockerFileCommandContent + "</VirtualComponent:principalFolder>\n");
										break;
								 }
								} // end for dockerfile
								 if(!output.contains("WORKDIR"))
									 writer.append("<VirtualComponent:principalFolder></VirtualComponent:principalFolder>\n");
						 
						 
						 if(output.contains("ADD"))
								for (int ligne = 0; ligne < someArray.length; ligne++) {
									someArray[ligne] = someArray[ligne].replaceAll("@", "");
									//System.out.println(someArray[ligne]);
									String[] tab = someArray[ligne].split(" ");
									String dockerFileCommand=tab[0].replaceAll(" ", "");
									String dockerFileCommandContent="";
								
								 if(dockerFileCommand.equals("ADD")){
									 nbADD++;
									 dockerFileCommandContent=someArray[ligne].substring(dockerFileCommand.length()+1, someArray[ligne].length());
									 writer.append("<VirtualComponent:importedFile>" + dockerFileCommandContent + "</VirtualComponent:importedFile>\n");
										break;
								 }
								} // end for dockerfile
								 if(!output.contains("ADD"))
									 writer.append("<VirtualComponent:importedFile></VirtualComponent:importedFile>\n");
				
						 			 
						 
						 if(output.contains("EXPOSE"))
								for (int ligne = 0; ligne < someArray.length; ligne++) {
									someArray[ligne] = someArray[ligne].replaceAll("@", "");
									//System.out.println(someArray[ligne]);
									String[] tab = someArray[ligne].split(" ");
									String dockerFileCommand=tab[0].replaceAll(" ", "");
									String dockerFileCommandContent="";
								
								 if(dockerFileCommand.equals("EXPOSE")){
									 nbEXPOSE++;
									 dockerFileCommandContent=someArray[ligne].substring(dockerFileCommand.length()+1, someArray[ligne].length());
									 writer.append("<VirtualComponent:port>" + dockerFileCommandContent + "</VirtualComponent:port>\n");
								break;
								 }
								} // end for dockerfile
								 if(!output.contains("EXPOSE"))
									 writer.append("<VirtualComponent:port></VirtualComponent:port>\n");
					 
						 
						 
						 if(output.contains("VOLUME"))
								for (int ligne = 0; ligne < someArray.length; ligne++) {
									someArray[ligne] = someArray[ligne].replaceAll("@", "");
									//System.out.println(someArray[ligne]);
									String[] tab = someArray[ligne].split(" ");
									String dockerFileCommand=tab[0].replaceAll(" ", "");
									String dockerFileCommandContent="";
								
								 if(dockerFileCommand.equals("VOLUME")){
									 nbVOLUME++;
									 dockerFileCommandContent=someArray[ligne].substring(dockerFileCommand.length()+1, someArray[ligne].length());
									 writer.append("<VirtualComponent:sharedFolder>" + dockerFileCommandContent + "</VirtualComponent:sharedFolder>\n");
										break;
								 }
								} // end for dockerfile
								 if(!output.contains("VOLUME"))
									 writer.append("<VirtualComponent:sharedFolder></VirtualComponent:sharedFolder>\n");
						 
						 
						 
						 
						 if(output.contains("CMD"))
								for (int ligne = 0; ligne < someArray.length; ligne++) {
									someArray[ligne] = someArray[ligne].replaceAll("@", "");
									//System.out.println(someArray[ligne]);
									String[] tab = someArray[ligne].split(" ");
									String dockerFileCommand=tab[0].replaceAll(" ", "");
									String dockerFileCommandContent="";
								
								 if(dockerFileCommand.equals("CMD")){
									 nbCMD++;
									 dockerFileCommandContent=someArray[ligne].substring(dockerFileCommand.length()+1, someArray[ligne].length());
									 writer.append("<VirtualComponent:initialRunArguments>" + dockerFileCommandContent + "</VirtualComponent:initialRunArguments>\n");
										break;
								 }
								} // end for dockerfile
								 if(!output.contains("CMD"))
									 writer.append("<VirtualComponent:initialRunArguments></VirtualComponent:initialRunArguments>\n");
						 
								 
								 
								 
								 
								 
								 if(output.contains("ENTRYPOINT"))
										for (int ligne = 0; ligne < someArray.length; ligne++) {
											someArray[ligne] = someArray[ligne].replaceAll("@", "");
											//System.out.println(someArray[ligne]);
											String[] tab = someArray[ligne].split(" ");
											String dockerFileCommand=tab[0].replaceAll(" ", "");
											String dockerFileCommandContent="";
										
										 if(dockerFileCommand.equals("ENTRYPOINT")){
											 nbENTRYPOINT++;
											 dockerFileCommandContent=someArray[ligne].substring(dockerFileCommand.length()+1, someArray[ligne].length());
											 writer.append("<VirtualComponent:initialRunCommands>" + dockerFileCommandContent + "</VirtualComponent:initialRunCommands>\n");
												break;
										 }
										} // end for dockerfile
										 if(!output.contains("ENTRYPOINT"))
											 writer.append("<VirtualComponent:initialRunCommands></VirtualComponent:initialRunCommands>\n");
								 
								
						 
						 if(output.contains("ENV"))
								for (int ligne = 0; ligne < someArray.length; ligne++) {
									someArray[ligne] = someArray[ligne].replaceAll("@", "");
									//System.out.println(someArray[ligne]);
									String[] tab = someArray[ligne].split(" ");
									String dockerFileCommand=tab[0].replaceAll(" ", "");
									String dockerFileCommandContent="";
								
								 if(dockerFileCommand.equals("ENV")){
									 nbENV++;
									 dockerFileCommandContent=someArray[ligne].substring(dockerFileCommand.length()+1, someArray[ligne].length());
									 writer.append("<VirtualComponent:environmentVariable>" + dockerFileCommandContent + "</VirtualComponent:environmentVariable>\n");
										break;
								 }
								} // end for dockerfile
								 if(!output.contains("ENV"))
									 writer.append("<VirtualComponent:environmentVariable></VirtualComponent:environmentVariable>\n");
						 
						 
						 if(output.contains("USER"))
								for (int ligne = 0; ligne < someArray.length; ligne++) {
									someArray[ligne] = someArray[ligne].replaceAll("@", "");
									//System.out.println(someArray[ligne]);
									String[] tab = someArray[ligne].split(" ");
									String dockerFileCommand=tab[0].replaceAll(" ", "");
									String dockerFileCommandContent="";
								
								 if(dockerFileCommand.equals("USER")){
									 nbUSER++;
									 dockerFileCommandContent=someArray[ligne].substring(dockerFileCommand.length()+1, someArray[ligne].length());
									 writer.append("<VirtualComponent:userUID>" + dockerFileCommandContent + "</VirtualComponent:userUID>\n");
										break;
								 }
								} // end for dockerfile
								 if(!output.contains("USER"))
									 writer.append("<VirtualComponent:userUID></VirtualComponent:userUID>\n");
						
						 
						}// end if equals image
						
					}// end for ecodes
				}// fin for "element"
			} catch (Exception e) {}
			writer.append("<VirtualComponent:tag />\n\n");
			}
			
			}// finfor "tags"
			
			
			
		}// finfor "allimages"
		 }catch(Exception e){}
		writer.append("<rdf:Description />\n\n");
		
			nbImage++;
		 }// For (all images)
		
		} // For (all images)
		 writer.append("<rdf:RDF />\n\n");
		 
		System.out.println("nbTag "+nbTag);
		System.out.println("nbImage "+nbImage);
		System.out.println("nbFROM "+nbFROM);
		System.out.println("nbWORKDIR "+nbWORKDIR);
		System.out.println("nbADD "+nbADD);
		System.out.println("nbEXPOSE "+nbEXPOSE);     
		System.out.println("nbVOLUME "+nbVOLUME);
		System.out.println("nbCMD "+nbCMD);
		System.out.println("nbENTRYPOINT "+nbENTRYPOINT);
		System.out.println("nbENV "+nbENV);
		System.out.println("nbUSER "+nbUSER);
		 
	} 

}



/*


	

*/	
