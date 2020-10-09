import processing.core.*;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.data.Table;

public class main  extends PApplet {
    public static void main(String[] args) { PApplet.main("main"); }
    float angleX,angleY,angleZ;
    String info;
    JSONObject jay;
    JSONObject jay2;
    Table table;
    boolean show;
    float r = 100;
    AlmindeligKnap btnHowTo;

    PImage bg ;
    PImage earth;

    PShape globe;

    @Override
    public void settings() {
        size(1280, 720,P3D);
    }

    @Override
    public void setup() {
        bg = loadImage("w.jpg");
        earth = loadImage("earth.jpg"); //prøv Albert.png og b4nny.png og earth.jpg
        //table = loadTable("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.csv", "header"); //der DER DER WAAHH WAAA SE MIG ***********************************
        jay = loadJSONObject("https://www.n2yo.com/rest/v1/satellite/positions/25544/41.702/-76.014/0/2/&apiKey=EVZW8Y-GY7GW3-U6FV47-4KH5");
        jay2 = loadJSONObject("https://www.n2yo.com/rest/v1/satellite/positions/25635/41.702/-76.014/0/2/&apiKey=EVZW8Y-GY7GW3-U6FV47-4KH5");
        info = "Shown satellites: \n" + hentText(jay) + "\n" + hentText(jay2);


        noStroke();

        globe = createShape(SPHERE, r);
        globe.setTexture(earth);
        System.out.println(jay.getJSONArray("positions"));
        System.out.println(jay2.getJSONArray("positions"));


        noStroke();

        // init them: (xPos, yPos, width, height)
    }

    @Override
    public void draw() {
        clear();
        background(bg);
        if (frameCount % 10000 == 0) {
            jay = loadJSONObject("https://www.n2yo.com/rest/v1/satellite/positions/25544/41.702/-76.014/0/2/&apiKey=EVZW8Y-GY7GW3-U6FV47-4KH5");
            jay2 = loadJSONObject("https://www.n2yo.com/rest/v1/satellite/positions/25635/41.702/-76.014/0/2/&apiKey=EVZW8Y-GY7GW3-U6FV47-4KH5");

        }

        lights();
        fill(250,150,0);
        textSize(35);
        text(info,50,50);
        text("Press the mouse to show satellite names \nPress and hold the arrow keys to turn the planet",50, height-80);
        textSize(23);
        translate((float) (width*0.5), (float)(height*0.5));
        if (keyPressed && keyCode == UP) {
            angleX += 0.05;
        }
        if (keyPressed && keyCode == DOWN) {
            angleX -= 0.05;
        }
        if (keyPressed && keyCode == RIGHT) {
            angleY += 0.05;
        }
        if (keyPressed && keyCode == LEFT) {
            angleY -= 0.05;
        }

        rotateX(angleX);
        rotateZ(angleZ);
        rotateY(angleY);



        fill(200);
        noStroke();
        shape(globe);
        drawBox(jay2, jay2);
        drawBox(jay,jay);


    }

    public void drawBox(JSONObject json, JSONObject json2){
        JSONArray positions = json.getJSONArray("positions");

        JSONObject o = positions.getJSONObject(0);

        float lat = o.getFloat("satlatitude");

        float lon = o.getFloat("satlongitude");

        float theta = radians(lat);

        float phi = radians(lon) + PI;

        float x = r*2 * cos(theta) * cos(phi)-40;

        float y = -r*2 * sin(theta);

        float z = -r*2 * cos(theta) * sin(phi);



        PVector pos = new PVector(x, y, z);

        PVector xaxis = new PVector(1, 0, 0);

        float angleb = PVector.angleBetween(xaxis, pos);

        PVector raxis = xaxis.cross(pos);






        pushMatrix();
        translate(x, y, z);

        rotate(angleb, raxis.x, raxis.y, raxis.z);


        if(show == false){



            fill(250,250,0);
            text( hentText(json) ,20,20,20);


            }fill(255);
        box(20, 20, 20);
        popMatrix();
    }
    public String hentText(JSONObject json2){
        JSONObject info = json2.getJSONObject("info");
        String name = info.getString("satname");
        int id = info.getInt("satid");
        String tag = name + ": " + id;
        return tag;
    }

    @Override
    public void mouseClicked() {

            show = !show;

    }
}