package me.tatarka.gsonvalue.processor;

import com.google.testing.compile.JavaFileObjects;
import me.tatarka.gsonvalue.GsonValueProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

@RunWith(JUnit4.class)
public class GsonValueProcessorTest {

    @Test
    public void emptyConstructor() {
        ASSERT.about(javaSource()).that(JavaFileObjects.forSourceString("test.Test",
                "package test;\n" +
                        "\n" +
                        "import me.tatarka.gsonvalue.annotations.GsonConstructor;\n" +
                        "\n" +
                        "public class Test {\n" +
                        "    @GsonConstructor\n" +
                        "    public Test() {\n" +
                        "    }\n" +
                        "}"))
                .processedWith(new GsonValueProcessor())
                .compilesWithoutError()
                .and().generatesSources(JavaFileObjects.forSourceString("test.ValueTypeAdapter_Test",
                "package test;\n" +
                        "\n" +
                        "import com.google.gson.Gson;\n" +
                        "import com.google.gson.TypeAdapter;\n" +
                        "import com.google.gson.reflect.TypeToken;\n" +
                        "import com.google.gson.stream.JsonReader;\n" +
                        "import com.google.gson.stream.JsonWriter;\n" +
                        "import java.io.IOException;\n" +
                        "\n" +
                        "public class ValueTypeAdapter_Test extends TypeAdapter<Test> {\n" +
                        "    public ValueTypeAdapter_Test(Gson gson, TypeToken<Test> typeToken) {\n" +
                        "    }\n" +
                        "    @Override\n" +
                        "    public void write(JsonWriter out, Test value) throws IOException {\n" +
                        "        out.beginObject();\n" +
                        "        out.endObject();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public Test read(JsonReader in) throws IOException {\n" +
                        "        in.skipValue();\n" +
                        "        return new Test();\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    public void oneArgConstructor() {
        ASSERT.about(javaSource()).that(JavaFileObjects.forSourceString("test.Test",
                "package test;\n" +
                        "\n" +
                        "import me.tatarka.gsonvalue.annotations.GsonConstructor;\n" +
                        "\n" +
                        "public class Test {\n" +
                        "    private final int arg;\n" +
                        "    @GsonConstructor\n" +
                        "    public Test(int arg) {\n" +
                        "        this.arg = arg;\n" +
                        "    }\n" +
                        "    \n" +
                        "    public int arg() {\n" +
                        "        return arg;\n" +
                        "    }\n" +
                        "}"))
                .processedWith(new GsonValueProcessor())
                .compilesWithoutError()
                .and().generatesSources(JavaFileObjects.forSourceString("test.ValueTypeAdapter_Test",
                "package test;\n" +
                        "\n" +
                        "import com.google.gson.Gson;\n" +
                        "import com.google.gson.TypeAdapter;\n" +
                        "import com.google.gson.reflect.TypeToken;\n" +
                        "import com.google.gson.stream.JsonReader;\n" +
                        "import com.google.gson.stream.JsonWriter;\n" +
                        "import java.io.IOException;\n" +
                        "\n" +
                        "public class ValueTypeAdapter_Test extends TypeAdapter<Test> {\n" +
                        "    private final TypeAdapter<Integer> adapter_arg;\n" +
                        "    public ValueTypeAdapter_Test(Gson gson, TypeToken<Test> typeToken) {\n" +
                        "        this.adapter_arg = gson.getAdapter(int.class);\n" +
                        "    }\n" +
                        "    @Override\n" +
                        "    public void write(JsonWriter out, Test value) throws IOException {\n" +
                        "        out.beginObject();\n" +
                        "        out.name(\"arg\");\n" +
                        "        adapter_arg.write(out, value.arg());\n" +
                        "        out.endObject();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public Test read(JsonReader in) throws IOException {\n" +
                        "        int _arg = 0;\n" +
                        "        in.beginObject();\n" +
                        "        while (in.hasNext()) {\n" +
                        "            switch (in.nextName()) {\n" +
                        "                case \"arg\":\n" +
                        "                    _arg = adapter_arg.read(in);\n" +
                        "                    break;\n" +
                        "                default:" +
                        "                    in.skipValue();" +
                        "            }\n" +
                        "        }\n" +
                        "        in.endObject();\n" +
                        "        return new Test(_arg);\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    public void oneArgMethod() {
        ASSERT.about(javaSource()).that(JavaFileObjects.forSourceString("test.Test",
                "package test;\n" +
                        "\n" +
                        "import me.tatarka.gsonvalue.annotations.GsonConstructor;\n" +
                        "\n" +
                        "public class Test {\n" +
                        "    private int arg;\n" +
                        "    @GsonConstructor\n" +
                        "    public static Test create(int arg) {\n" +
                        "        Test test = new Test();\n" +
                        "        test.arg = arg;\n" +
                        "        return test;\n" +
                        "    }\n" +
                        "    public int arg() {\n" +
                        "        return arg;\n" +
                        "    }\n" +
                        "}"))
                .processedWith(new GsonValueProcessor())
                .compilesWithoutError()
                .and().generatesSources(JavaFileObjects.forSourceString("test.ValueTypeAdapter_Test",
                "package test;\n" +
                        "\n" +
                        "import com.google.gson.Gson;\n" +
                        "import com.google.gson.TypeAdapter;\n" +
                        "import com.google.gson.reflect.TypeToken;\n" +
                        "import com.google.gson.stream.JsonReader;\n" +
                        "import com.google.gson.stream.JsonWriter;\n" +
                        "import java.io.IOException;\n" +
                        "\n" +
                        "public class ValueTypeAdapter_Test extends TypeAdapter<Test> {\n" +
                        "    private final TypeAdapter<Integer> adapter_arg;\n" +
                        "    public ValueTypeAdapter_Test(Gson gson, TypeToken<Test> typeToken) {\n" +
                        "        this.adapter_arg = gson.getAdapter(int.class);\n" +
                        "    }\n" +
                        "    @Override\n" +
                        "    public void write(JsonWriter out, Test value) throws IOException {\n" +
                        "        out.beginObject();\n" +
                        "        out.name(\"arg\");\n" +
                        "        adapter_arg.write(out, value.arg());\n" +
                        "        out.endObject();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public Test read(JsonReader in) throws IOException {\n" +
                        "        int _arg = 0;\n" +
                        "        in.beginObject();\n" +
                        "        while (in.hasNext()) {\n" +
                        "            switch (in.nextName()) {\n" +
                        "                case \"arg\":\n" +
                        "                    _arg = adapter_arg.read(in);\n" +
                        "                    break;\n" +
                        "                default:" +
                        "                    in.skipValue();" +
                        "            }\n" +
                        "        }\n" +
                        "        in.endObject();\n" +
                        "        return Test.create(_arg);\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    public void oneArgBuilderConstructor() {
        ASSERT.about(javaSource()).that(JavaFileObjects.forSourceString("test.Test",
                "package test;\n" +
                        "\n" +
                        "import me.tatarka.gsonvalue.annotations.GsonBuilder;\n" +
                        "\n" +
                        "public class Test {\n" +
                        "    private int arg;\n" +
                        "    public int arg() {\n" +
                        "        return arg;\n" +
                        "    }\n" +
                        "    \n" +
                        "    public static class Builder {\n" +
                        "        private int arg;\n" +
                        "        @GsonBuilder\n" +
                        "        public Builder() {\n" +
                        "        }\n" +
                        "        \n" +
                        "        public Builder arg(int arg) {\n" +
                        "            this.arg = arg;\n" +
                        "            return this;\n" +
                        "        }\n" +
                        "        \n" +
                        "        public Test build() {\n" +
                        "            Test test = new Test();\n" +
                        "            test.arg = arg;\n" +
                        "            return test;\n" +
                        "        }\n" +
                        "    }\n" +
                        "}"))
                .processedWith(new GsonValueProcessor())
                .compilesWithoutError()
                .and().generatesSources(JavaFileObjects.forSourceString("test.ValueTypeAdapter_Test",
                "package test;\n" +
                        "\n" +
                        "import com.google.gson.Gson;\n" +
                        "import com.google.gson.TypeAdapter;\n" +
                        "import com.google.gson.reflect.TypeToken;\n" +
                        "import com.google.gson.stream.JsonReader;\n" +
                        "import com.google.gson.stream.JsonWriter;\n" +
                        "import java.io.IOException;\n" +
                        "\n" +
                        "public class ValueTypeAdapter_Test extends TypeAdapter<Test> {\n" +
                        "    private final TypeAdapter<Integer> adapter_arg;\n" +
                        "    public ValueTypeAdapter_Test(Gson gson, TypeToken<Test> typeToken) {\n" +
                        "        this.adapter_arg = gson.getAdapter(int.class);\n" +
                        "    }\n" +
                        "    @Override\n" +
                        "    public void write(JsonWriter out, Test value) throws IOException {\n" +
                        "        out.beginObject();\n" +
                        "        out.name(\"arg\");\n" +
                        "        adapter_arg.write(out, value.arg());\n" +
                        "        out.endObject();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public Test read(JsonReader in) throws IOException {\n" +
                        "        int _arg = 0;\n" +
                        "        in.beginObject();\n" +
                        "        while (in.hasNext()) {\n" +
                        "            switch (in.nextName()) {\n" +
                        "                case \"arg\":\n" +
                        "                    _arg = adapter_arg.read(in);\n" +
                        "                    break;\n" +
                        "                default:" +
                        "                    in.skipValue();" +
                        "            }\n" +
                        "        }\n" +
                        "        in.endObject();\n" +
                        "        return new Test.Builder()\n" +
                        "                .arg(_arg)\n" +
                        "                .build();\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    public void oneArgBuilderMethod() {
        ASSERT.about(javaSource()).that(JavaFileObjects.forSourceString("test.Test",
                "package test;\n" +
                        "\n" +
                        "import me.tatarka.gsonvalue.annotations.GsonBuilder;\n" +
                        "\n" +
                        "public class Test {\n" +
                        "    private int arg;\n" +
                        "    public int arg() {\n" +
                        "        return arg;\n" +
                        "    }\n" +
                        "    @GsonBuilder\n" +
                        "    public static Builder builder() {\n" +
                        "        return new Builder();\n" +
                        "    }\n" +
                        "    \n" +
                        "    public static class Builder {\n" +
                        "        private int arg;\n" +
                        "        \n" +
                        "        public Builder arg(int arg) {\n" +
                        "            this.arg = arg;\n" +
                        "            return this;\n" +
                        "        }\n" +
                        "        \n" +
                        "        public Test build() {\n" +
                        "            Test test = new Test();\n" +
                        "            test.arg = arg;\n" +
                        "            return test;\n" +
                        "        }\n" +
                        "    }\n" +
                        "}"))
                .processedWith(new GsonValueProcessor())
                .compilesWithoutError()
                .and().generatesSources(JavaFileObjects.forSourceString("test.ValueTypeAdapter_Test",
                "package test;\n" +
                        "\n" +
                        "import com.google.gson.Gson;\n" +
                        "import com.google.gson.TypeAdapter;\n" +
                        "import com.google.gson.reflect.TypeToken;\n" +
                        "import com.google.gson.stream.JsonReader;\n" +
                        "import com.google.gson.stream.JsonWriter;\n" +
                        "import java.io.IOException;\n" +
                        "\n" +
                        "public class ValueTypeAdapter_Test extends TypeAdapter<Test> {\n" +
                        "    private final TypeAdapter<Integer> adapter_arg;\n" +
                        "    public ValueTypeAdapter_Test(Gson gson, TypeToken<Test> typeToken) {\n" +
                        "        this.adapter_arg = gson.getAdapter(int.class);\n" +
                        "    }\n" +
                        "    @Override\n" +
                        "    public void write(JsonWriter out, Test value) throws IOException {\n" +
                        "        out.beginObject();\n" +
                        "        out.name(\"arg\");\n" +
                        "        adapter_arg.write(out, value.arg());\n" +
                        "        out.endObject();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public Test read(JsonReader in) throws IOException {\n" +
                        "        int _arg = 0;\n" +
                        "        in.beginObject();\n" +
                        "        while (in.hasNext()) {\n" +
                        "            switch (in.nextName()) {\n" +
                        "                case \"arg\":\n" +
                        "                    _arg = adapter_arg.read(in);\n" +
                        "                    break;\n" +
                        "                default:" +
                        "                    in.skipValue();" +
                        "            }\n" +
                        "        }\n" +
                        "        in.endObject();\n" +
                        "        return Test.builder()\n" +
                        "                .arg(_arg)\n" +
                        "                .build();\n" +
                        "    }\n" +
                        "}"));
    }

    public void oneRequiredArgBuilderConstructor() {
        ASSERT.about(javaSource()).that(JavaFileObjects.forSourceString("test.Test",
                "package test;\n" +
                        "\n" +
                        "import me.tatarka.gsonvalue.annotations.GsonBuilder;\n" +
                        "\n" +
                        "public class Test {\n" +
                        "    private int arg;\n" +
                        "    public int arg() {\n" +
                        "        return arg;\n" +
                        "    }\n" +
                        "    \n" +
                        "    public static class Builder {\n" +
                        "        private int arg;\n" +
                        "        @GsonBuilder\n" +
                        "        public Builder(int arg) {\n" +
                        "            this.arg = arg;\n" +
                        "        }\n" +
                        "        \n" +
                        "        public Test build() {\n" +
                        "            Test test = new Test();\n" +
                        "            test.arg = arg;\n" +
                        "            return test;\n" +
                        "        }\n" +
                        "    }\n" +
                        "}"))
                .processedWith(new GsonValueProcessor())
                .compilesWithoutError()
                .and().generatesSources(JavaFileObjects.forSourceString("test.ValueTypeAdapter_Test",
                "package test;\n" +
                        "\n" +
                        "import com.google.gson.Gson;\n" +
                        "import com.google.gson.TypeAdapter;\n" +
                        "import com.google.gson.reflect.TypeToken;\n" +
                        "import com.google.gson.stream.JsonReader;\n" +
                        "import com.google.gson.stream.JsonWriter;\n" +
                        "import java.io.IOException;\n" +
                        "\n" +
                        "public class ValueTypeAdapter_Test extends TypeAdapter<Test> {\n" +
                        "    private final TypeAdapter<Integer> adapter_arg;\n" +
                        "    public ValueTypeAdapter_Test(Gson gson, TypeToken<Test> typeToken) {\n" +
                        "        this.adapter_arg = gson.getAdapter(int.class);\n" +
                        "    }\n" +
                        "    @Override\n" +
                        "    public void write(JsonWriter out, Test value) throws IOException {\n" +
                        "        out.beginObject();\n" +
                        "        out.name(\"arg\");\n" +
                        "        adapter_arg.write(out, value.arg());\n" +
                        "        out.endObject();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public Test read(JsonReader in) throws IOException {\n" +
                        "        int _arg = 0;\n" +
                        "        in.beginObject();\n" +
                        "        while (in.hasNext()) {\n" +
                        "            switch (in.nextName()) {\n" +
                        "                case \"arg\":\n" +
                        "                    _arg = adapter_arg.read(in);\n" +
                        "                    break;\n" +
                        "                default:" +
                        "                    in.skipValue();" +
                        "            }\n" +
                        "        }\n" +
                        "        in.endObject();\n" +
                        "        return new Test.Builder(_arg)\n" +
                        "                .build();\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    public void oneRequiredArgBuilderMethod() {
        ASSERT.about(javaSource()).that(JavaFileObjects.forSourceString("test.Test",
                "package test;\n" +
                        "\n" +
                        "import com.google.gson.annotations.SerializedName;\n" +
                        "import me.tatarka.gsonvalue.annotations.GsonBuilder;\n" +
                        "\n" +
                        "public class Test {\n" +
                        "    @SerializedName(\"named\")\n" +
                        "    private int arg;\n" +
                        "    public int arg() {\n" +
                        "        return arg;\n" +
                        "    }\n" +
                        "    @GsonBuilder\n" +
                        "    public static Builder builder(int arg) {\n" +
                        "        return new Builder()\n" +
                        "                .arg(arg);\n" +
                        "    }\n" +
                        "    \n" +
                        "    public static class Builder {\n" +
                        "        private int arg;\n" +
                        "        \n" +
                        "        public Builder arg(int arg) {\n" +
                        "            this.arg = arg;\n" +
                        "            return this;\n" +
                        "        }\n" +
                        "        \n" +
                        "        public Test build() {\n" +
                        "            Test test = new Test();\n" +
                        "            test.arg = arg;\n" +
                        "            return test;\n" +
                        "        }\n" +
                        "    }\n" +
                        "}"))
                .processedWith(new GsonValueProcessor())
                .compilesWithoutError()
                .and().generatesSources(JavaFileObjects.forSourceString("test.ValueTypeAdapter_Test",
                "package test;\n" +
                        "\n" +
                        "import com.google.gson.Gson;\n" +
                        "import com.google.gson.TypeAdapter;\n" +
                        "import com.google.gson.reflect.TypeToken;\n" +
                        "import com.google.gson.stream.JsonReader;\n" +
                        "import com.google.gson.stream.JsonWriter;\n" +
                        "import java.io.IOException;\n" +
                        "\n" +
                        "public class ValueTypeAdapter_Test extends TypeAdapter<Test> {\n" +
                        "    private final TypeAdapter<Integer> adapter_arg;\n" +
                        "    public ValueTypeAdapter_Test(Gson gson, TypeToken<Test> typeToken) {\n" +
                        "        this.adapter_arg = gson.getAdapter(int.class);\n" +
                        "    }\n" +
                        "    @Override\n" +
                        "    public void write(JsonWriter out, Test value) throws IOException {\n" +
                        "        out.beginObject();\n" +
                        "        out.name(\"named\");\n" +
                        "        adapter_arg.write(out, value.arg());\n" +
                        "        out.endObject();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public Test read(JsonReader in) throws IOException {\n" +
                        "        int _arg = 0;\n" +
                        "        in.beginObject();\n" +
                        "        while (in.hasNext()) {\n" +
                        "            switch (in.nextName()) {\n" +
                        "                case \"named\":\n" +
                        "                    _arg = adapter_arg.read(in);\n" +
                        "                    break;\n" +
                        "                default:" +
                        "                    in.skipValue();" +
                        "            }\n" +
                        "        }\n" +
                        "        in.endObject();\n" +
                        "        return Test.builder(_arg)\n" +
                        "                .build();\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    public void oneComplexArgConstructor() {
        ASSERT.about(javaSource()).that(JavaFileObjects.forSourceString("test.Test",
                "package test;\n" +
                        "\n" +
                        "import me.tatarka.gsonvalue.annotations.GsonConstructor;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "public class Test {\n" +
                        "    private final List<String> arg;\n" +
                        "    @GsonConstructor\n" +
                        "    public Test(List<String> arg) {\n" +
                        "        this.arg = arg;\n" +
                        "    }\n" +
                        "    \n" +
                        "    public List<String> arg() {\n" +
                        "        return arg;\n" +
                        "    }\n" +
                        "}"))
                .processedWith(new GsonValueProcessor())
                .compilesWithoutError()
                .and().generatesSources(JavaFileObjects.forSourceString("test.ValueTypeAdapter_Test",
                "package test;\n" +
                        "\n" +
                        "import com.google.gson.Gson;\n" +
                        "import com.google.gson.TypeAdapter;\n" +
                        "import com.google.gson.reflect.TypeToken;\n" +
                        "import com.google.gson.stream.JsonReader;\n" +
                        "import com.google.gson.stream.JsonWriter;\n" +
                        "import java.io.IOException;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "public class ValueTypeAdapter_Test extends TypeAdapter<Test> {\n" +
                        "    private final TypeAdapter<List<String>> adapter_arg;\n" +
                        "    public ValueTypeAdapter_Test(Gson gson, TypeToken<Test> typeToken) {\n" +
                        "        this.adapter_arg = gson.getAdapter(new TypeToken<List<String>>() {});\n" +
                        "    }\n" +
                        "    @Override\n" +
                        "    public void write(JsonWriter out, Test value) throws IOException {\n" +
                        "        out.beginObject();\n" +
                        "        out.name(\"arg\");\n" +
                        "        adapter_arg.write(out, value.arg());\n" +
                        "        out.endObject();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public Test read(JsonReader in) throws IOException {\n" +
                        "        List<String> _arg = null;\n" +
                        "        in.beginObject();\n" +
                        "        while (in.hasNext()) {\n" +
                        "            switch (in.nextName()) {\n" +
                        "                case \"arg\":\n" +
                        "                    _arg = adapter_arg.read(in);\n" +
                        "                    break;\n" +
                        "                default:" +
                        "                    in.skipValue();" +
                        "            }\n" +
                        "        }\n" +
                        "        in.endObject();\n" +
                        "        return new Test(_arg);\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    public void genericConstructor() {
        ASSERT.about(javaSource()).that(JavaFileObjects.forSourceString("test.Test",
                "package test;\n" +
                        "\n" +
                        "import me.tatarka.gsonvalue.annotations.GsonConstructor;\n" +
                        "\n" +
                        "public class Test<T> {\n" +
                        "    private final T arg;\n" +
                        "    @GsonConstructor\n" +
                        "    public Test(T arg) {\n" +
                        "        this.arg = arg;\n" +
                        "    }\n" +
                        "    \n" +
                        "    public T arg() {\n" +
                        "        return arg;\n" +
                        "    }\n" +
                        "}"))
                .processedWith(new GsonValueProcessor())
                .compilesWithoutError()
                .and().generatesSources(JavaFileObjects.forSourceString("test.ValueTypeAdapter_Test",
                "package test;\n" +
                        "\n" +
                        "import com.google.gson.Gson;\n" +
                        "import com.google.gson.TypeAdapter;\n" +
                        "import com.google.gson.reflect.TypeToken;\n" +
                        "import com.google.gson.stream.JsonReader;\n" +
                        "import com.google.gson.stream.JsonWriter;\n" +
                        "import java.io.IOException;\n" +
                        "import java.lang.reflect.ParameterizedType;\n" +
                        "\n" +
                        "public class ValueTypeAdapter_Test<T> extends TypeAdapter<Test<T>> {\n" +
                        "    private final TypeAdapter<T> adapter_arg;\n" +
                        "    public ValueTypeAdapter_Test(Gson gson, TypeToken<Test<T>> typeToken) {\n" +
                        "        this.adapter_arg = gson.getAdapter((TypeToken<T>) TypeToken.get(((ParameterizedType)typeToken.getType()).getActualTypeArguments()[0]));" +
                        "    }\n" +
                        "    @Override\n" +
                        "    public void write(JsonWriter out, Test<T> value) throws IOException {\n" +
                        "        out.beginObject();\n" +
                        "        out.name(\"arg\");\n" +
                        "        adapter_arg.write(out, value.arg());\n" +
                        "        out.endObject();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public Test<T> read(JsonReader in) throws IOException {\n" +
                        "        T _arg = null;\n" +
                        "        in.beginObject();\n" +
                        "        while (in.hasNext()) {\n" +
                        "            switch (in.nextName()) {\n" +
                        "                case \"arg\":\n" +
                        "                    _arg = adapter_arg.read(in);\n" +
                        "                    break;\n" +
                        "                default:" +
                        "                    in.skipValue();" +
                        "            }\n" +
                        "        }\n" +
                        "        in.endObject();\n" +
                        "        return new Test<T>(_arg);\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    public void complexGenericConstructor() {
        ASSERT.about(javaSource()).that(JavaFileObjects.forSourceString("test.Test",
                "package test;\n" +
                        "\n" +
                        "import me.tatarka.gsonvalue.annotations.GsonConstructor;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "public class Test<T> {\n" +
                        "    private final List<T> arg;\n" +
                        "    @GsonConstructor\n" +
                        "    public Test(List<T> arg) {\n" +
                        "        this.arg = arg;\n" +
                        "    }\n" +
                        "    \n" +
                        "    public List<T> arg() {\n" +
                        "        return arg;\n" +
                        "    }\n" +
                        "}"))
                .processedWith(new GsonValueProcessor())
                .compilesWithoutError()
                .and().generatesSources(JavaFileObjects.forSourceString("test.ValueTypeAdapter_Test",
                "package test;\n" +
                        "\n" +
                        "import com.google.gson.Gson;\n" +
                        "import com.google.gson.TypeAdapter;\n" +
                        "import com.google.gson.reflect.TypeToken;\n" +
                        "import com.google.gson.stream.JsonReader;\n" +
                        "import com.google.gson.stream.JsonWriter;\n" +
                        "import java.io.IOException;\n" +
                        "import java.lang.reflect.ParameterizedType;\n" +
                        "import java.util.List;\n" +
                        "import me.tatarka.gsonvalue.internal.Types;\n" +
                        "\n" +
                        "public class ValueTypeAdapter_Test<T> extends TypeAdapter<Test<T>> {\n" +
                        "    private final TypeAdapter<List<T>> adapter_arg;\n" +
                        "    public ValueTypeAdapter_Test(Gson gson, TypeToken<Test<T>> typeToken) {\n" +
                        "        this.adapter_arg = gson.getAdapter((TypeToken<List<T>>) TypeToken.get(Types.newParameterizedType(List.class, ((ParameterizedType)typeToken.getType()).getActualTypeArguments()[0])));\n" +
                        "    }\n" +
                        "    @Override\n" +
                        "    public void write(JsonWriter out, Test<T> value) throws IOException {\n" +
                        "        out.beginObject();\n" +
                        "        out.name(\"arg\");\n" +
                        "        adapter_arg.write(out, value.arg());\n" +
                        "        out.endObject();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public Test<T> read(JsonReader in) throws IOException {\n" +
                        "        List<T> _arg = null;\n" +
                        "        in.beginObject();\n" +
                        "        while (in.hasNext()) {\n" +
                        "            switch (in.nextName()) {\n" +
                        "                case \"arg\":\n" +
                        "                    _arg = adapter_arg.read(in);\n" +
                        "                    break;\n" +
                        "                default:" +
                        "                    in.skipValue();" +
                        "            }\n" +
                        "        }\n" +
                        "        in.endObject();\n" +
                        "        return new Test<T>(_arg);\n" +
                        "    }\n" +
                        "}"));
    }
}