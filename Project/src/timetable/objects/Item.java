package timetable.objects;

public class Item {
    private String sort;
    private String name;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }
}
