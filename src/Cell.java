import java.util.ArrayList;
import java.util.List;

public class Cell {
    private List<Point> list;

    public Cell(){
        list = new ArrayList<>();
    }

    public List<Point> getList() {
        return list;
    }

    public void setList(List<Point> list) {
        this.list = list;
    }
}
