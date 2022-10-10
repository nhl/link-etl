package com.nhl.link.move.unit.cayenne.ti.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.cayenne.exp.property.PropertyFactory;
import org.apache.cayenne.exp.property.StringProperty;

import com.nhl.link.move.unit.cayenne.ti.TiSuper;

/**
 * Class _TiSub1 was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _TiSub1 extends TiSuper {

    private static final long serialVersionUID = 1L;

    public static final String ID_PK_COLUMN = "id";

    public static final StringProperty<String> SUB_KEY = PropertyFactory.createString("subKey", String.class);
    public static final StringProperty<String> SUBP1 = PropertyFactory.createString("subp1", String.class);

    protected String subKey;
    protected String subp1;


    public void setSubKey(String subKey) {
        beforePropertyWrite("subKey", this.subKey, subKey);
        this.subKey = subKey;
    }

    public String getSubKey() {
        beforePropertyRead("subKey");
        return this.subKey;
    }

    public void setSubp1(String subp1) {
        beforePropertyWrite("subp1", this.subp1, subp1);
        this.subp1 = subp1;
    }

    public String getSubp1() {
        beforePropertyRead("subp1");
        return this.subp1;
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "subKey":
                return this.subKey;
            case "subp1":
                return this.subp1;
            default:
                return super.readPropertyDirectly(propName);
        }
    }

    @Override
    public void writePropertyDirectly(String propName, Object val) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch (propName) {
            case "subKey":
                this.subKey = (String)val;
                break;
            case "subp1":
                this.subp1 = (String)val;
                break;
            default:
                super.writePropertyDirectly(propName, val);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        writeSerialized(out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        readSerialized(in);
    }

    @Override
    protected void writeState(ObjectOutputStream out) throws IOException {
        super.writeState(out);
        out.writeObject(this.subKey);
        out.writeObject(this.subp1);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.subKey = (String)in.readObject();
        this.subp1 = (String)in.readObject();
    }

}
