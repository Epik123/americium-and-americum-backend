package cc.americium.command;

import cc.americium.Z_;

public final class Context {
    private String _command;
    private String[] _args;
    private Z_ _z;

    public String getCommand() {
        return this._command;
    }

    public void setCommand(String cmd) {
        this._command = cmd;
    }

    public String[] getArgs() {
        return this._args;
    }

    public void setArgs(String[] args) {
        this._args = args;
    }

    public Z_ getZ_() {
        return this._z;
    }

    public void setZ_(Z_ z) {
        this._z = z;
    }
}