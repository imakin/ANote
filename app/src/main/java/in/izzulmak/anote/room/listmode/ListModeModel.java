package in.izzulmak.anote.room.listmode;


import in.izzulmak.anote.core.ModelMain;

/**
 * Created by Izzulmakin on 12/09/16.
 * Model and action (common to be called Controller) specific to the room
 */
public class ListModeModel extends ModelMain {
    public static void init() {
        provisioningAdd("ListObject", "id encrypteddata", "id", false, true);
        provisioningApply();
    }

    public static void load() {

    }
}
