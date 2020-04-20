package es.upm.dit.isst.soa;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.*;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("getTrip1")
public class Test1 {

	@GET 
	@Produces({"text/html"})
	public String getTrip(@DefaultValue("MX") @QueryParam("origen") String origen, 
						  @QueryParam("destino") String destino){
		
		//1) Construir la petición: https://restcountries.eu/rest/v1/alpha?codes=mx
		Client client = ClientBuilder.newClient();
		String infoPaisOri = client.target("http://api.openweathermap.org/data/2.5/weather?units=metric&APPID=b79615aad18a5fe7e6c1f9df0cde0819&")		
		      .queryParam("q", origen)
              .request()
              .get(String.class);
        //2) Recuperar el JSON devuelto (es un array en este caso) y procesarlo para obtener la información que se desea
		infoPaisOri="["+infoPaisOri+"]";
        JSONArray arr = new JSONArray(infoPaisOri);
        //La capital está en la primera posición (0) del array devuelto, en el atributo 'capital'
        int temperatura = arr.getJSONObject(0).getJSONObject("main").getInt("temp");
        String country = arr.getJSONObject(0).getJSONObject("sys").getString("country");
        //Recuperamos el primer prefijo telefónico que encontremos (array con nombre 'callingCodes')
        //String prefijoInternacional = arr.getJSONObject(0).getJSONArray("callingCodes").getString(0);

        //3) Cerrar la conexión
        String moneda = client.target("https://restcountries.eu/rest/v2/alpha/")
        		.queryParam("codes",country)
        		.request()
        		.get(String.class);
        JSONArray err = new JSONArray(moneda);
        String moneda1 = err.getJSONObject(0).getJSONArray("currencies").getJSONObject(0).getString("name");
        client.close();
        
        return "Enhorabuena, tu viaje es de '"+origen+"' a '"+ destino +"'. "+origen+" tiene una temperatura '"+temperatura+"' y pertenece a "+country+" con moneda "+moneda1;
	}
}