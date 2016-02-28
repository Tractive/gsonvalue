package me.tatarka.gsonvalue.model.deserialize;

import me.tatarka.gsonvalue.annotations.GsonBuilder;

public class StandaloneBuilder {

    public static class Class {
        private final int arg;

        Class(int arg) {
            this.arg = arg;
        }
    }

    public static class Builder {
        private int arg;

        @GsonBuilder
        public Builder() {
        }

        public Builder arg(int arg) {
            this.arg = arg;
            return this;
        }

        public Class build() {
            return new Class(arg);
        }
    }
}
