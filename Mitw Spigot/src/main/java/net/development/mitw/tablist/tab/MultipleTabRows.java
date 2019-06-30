package net.development.mitw.tablist.tab;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.development.mitw.tablist.util.StringUtil;

import java.util.function.Supplier;

@Getter
@Setter
public class MultipleTabRows implements MultipleRows {

    private MultipleRowObj[] multipleRowObjs;
    private final int length;
    private final Supplier<String[]> rowGetter;

    public MultipleTabRows(int length, Supplier<String[]> rowGetter) {
        this.length = length;
        this.rowGetter = rowGetter;
    }

    @Override
    public void updateRows() {
        String[] rows = this.getRows();
        for (int i = 0; i < multipleRowObjs.length; i++) {
            if (i > rows.length - 1) {
                multipleRowObjs[i].getTeam().setPrefix("");
                multipleRowObjs[i].getTeam().setSuffix("");
                continue;
            }
            StringUtil.Entry string = StringUtil.split(rows[i]);
            multipleRowObjs[i].getTeam().setPrefix(string.getLeft());
            multipleRowObjs[i].getTeam().setSuffix(string.getRight());
        }
    }

    @Override
    public String[] getRows() {
        return rowGetter.get();
    }

    @Override
    public MultipleRowObj getFromSlot(int slot) {
        for (MultipleRowObj obj : multipleRowObjs) {
            if (obj.getSlot() == slot) {
                return obj;
            }
        }
        return null;
    }
}
