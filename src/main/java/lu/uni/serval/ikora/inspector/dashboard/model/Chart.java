package lu.uni.serval.ikora.inspector.dashboard.model;

public class Chart {
    private final String id;
    private final String name;
    private int height;

    protected Chart(String id, String name){
        this.id = id;
        this.name = name;
        this.height = 300;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl(){
        return String.format("js/%s.js", getId());
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
