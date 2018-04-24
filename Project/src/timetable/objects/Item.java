package timetable.objects;

public class Item {
    private String sort, name;
    private Integer id;

    public Item(String sort, String name, Integer id) {
        this.name = name;
        this.sort = sort;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSort() {
        return sort;
    }
}
