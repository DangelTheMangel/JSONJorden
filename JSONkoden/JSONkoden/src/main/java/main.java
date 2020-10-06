import processing.core.*;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.data.Table;
import processing.data.TableRow;

public class main  extends PApplet {
    public static void main(String[] args) { PApplet.main("main"); }
    float angle;

    JSONObject jay;
    JSONObject jay2;
    Table table;

    float r = 100;



    PImage earth;

    PShape globe;

    @Override
    public void settings() {
        size(600, 600,P3D);
    }

    @Override
    public void setup() {
        earth = loadImage("earth.jpg"); //prøv Albert.png og b4nny.png og earth.jpg

        // table = loadTable("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/significant_day.csv", "header");

        table = loadTable("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.csv", "header"); //der DER DER WAAHH WAAA SE MIG ***********************************

        jay = loadJSONObject("https://www.n2yo.com/rest/v1/satellite/positions/25544/41.702/-76.014/0/2/&apiKey=EVZW8Y-GY7GW3-U6FV47-4KH5");
        jay2 = loadJSONObject("https://www.n2yo.com/rest/v1/satellite/positions/25635/41.702/-76.014/0/2/&apiKey=EVZW8Y-GY7GW3-U6FV47-4KH5");


        noStroke();

        globe = createShape(SPHERE, r);
        globe.setTexture(earth);
        System.out.println(jay.getJSONArray("positions"));
        System.out.println(jay2.getJSONArray("positions"));
    }

    @Override
    public void draw() {
        background(51);

        translate((float) (width*0.5), (float)(height*0.5));

        rotateZ(angle);
        rotateY(angle);


        angle += 0.05;



        lights();

        fill(200);

        noStroke();

        //sphere(r);

        shape(globe);




        for (TableRow row : table.rows()) {

         drawBox(jay2, jay2);
         drawBox(jay,jay);

        }
    }

    public void drawBox(JSONObject json, JSONObject json2){
        JSONArray positions = json.getJSONArray("positions");

        JSONObject o = positions.getJSONObject(0);

        float lat = o.getFloat("satlatitude");

        float lon = o.getFloat("satlongitude");

        float theta = radians(lat);

        float phi = radians(lon) + PI;

        float x = r * cos(theta) * cos(phi)-40;

        float y = -r * sin(theta);

        float z = -r * cos(theta) * sin(phi);



        PVector pos = new PVector(x, y, z);

        PVector xaxis = new PVector(1, 0, 0);

        float angleb = PVector.angleBetween(xaxis, pos);

        PVector raxis = xaxis.cross(pos);






        //
        if(mousePressed){
        JSONObject info = json2.getJSONObject("info");
        String name = info.getString("satname");
        int id = info.getInt("satid");
        String tag = name + ": " + id;
        pushMatrix();

        translate(x, y, z);

        rotate(angleb, raxis.x, raxis.y, raxis.z);

        fill(255);
        text( tag ,20,20,20);
        box(20, 20, 20);

        popMatrix();}
    }

}


