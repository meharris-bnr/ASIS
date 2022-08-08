package asis;

import skyproc.ModListing;
import skyproc.NPC_;

import java.util.ArrayList;

/**
 * @author pc tech
 */
public class RandomActor {

    private final NPC_ NPC;
    private final ArrayList<Integer> groups;

    public RandomActor(NPC_ n, ArrayList<Integer> grouplist) {

        this.NPC = n;
        this.groups = grouplist;
    }

    public String getEDID() {
        return NPC.getEDID();
    }

    public ModListing getModMaster() {
        return NPC.getFormMaster();
    }

    public ArrayList<Integer> getGroupList() {
        return groups;
    }

    public String getFormStr() {
        return NPC.getFormStr();
    }

    public NPC_ getNPC() {
        return NPC;
    }
}
