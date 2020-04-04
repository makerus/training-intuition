package ru.katagarov.traningintuition;
import java.util.ArrayList;

class Generator {

    static int random(int from, int to) {
        return Math.round(Math.round((Math.random() * (to - from)) + from));
    }

    static String getRandomFruit() {
        ArrayList<String> fruit = new ArrayList<>();

        fruit.add("\uD83C\uDF4A");
        fruit.add("\uD83C\uDF4B");
        fruit.add("\uD83C\uDF4D");
        fruit.add("\uD83C\uDF4E");
        fruit.add("\uD83C\uDF4F");
        fruit.add("\uD83C\uDF50");
        fruit.add("\uD83C\uDF51");

        return fruit.get(Generator.random(0, fruit.size() - 1));
    }
}
