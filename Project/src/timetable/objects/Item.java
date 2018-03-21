package timetable.objects;

public class Item {
    private String sort;
    private String name;

    public Item(String sort, String name){
        this.name = name;
        this.sort = sort;
    }

    public String getName(){
        return name;
    }

    public String getSort(){
        return sort;
    }
}
