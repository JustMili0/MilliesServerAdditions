package net.justmili.corelibs.config.items;

public final class CommentItem implements ConfigItem {
    private final String comment;

    public CommentItem(String comment) {
        this.comment = comment;
    }

    public String comment() { return comment; }
}