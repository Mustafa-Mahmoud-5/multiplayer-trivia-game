package org.game.server.repositories;

import org.game.common.models.Question;

import java.util.*;
import java.util.stream.Collectors;

public class QuestionRepo {
    List<Question> questions = new ArrayList<>();

    // TODO(Abdullah): implement the repo, we have GSON library installed, check pom.xml
    public void load(String users) {
        // extract json, store in memory
        this.generateSampleData();
    }

    public List<Question> getAll() {
        return questions;
    }

    public List<Question> filterByCategory(String category) {
        return questions.stream().filter(q -> q.getCategory().equalsIgnoreCase(category)).collect(Collectors.toList());
    }

    public List<Question> filterByCriteria(String category, String difficulty) {
        return questions.stream().filter(q ->
                        q.getCategory().equalsIgnoreCase(category) && q.getDifficulty().equalsIgnoreCase(difficulty))
                .collect(Collectors.toList());
    }

    public List<Question> filterByDifficulty(String difficulty) {
        return questions.stream().filter(q -> q.getDifficulty().equalsIgnoreCase(difficulty)).collect(Collectors.toList());
    }

    public List<String> getCategories() {
        return questions.stream().map(Question::getCategory).distinct()
                .collect(Collectors.toList());
    }

    public List<String> getDifficulties() {
        return questions.stream().map(Question::getDifficulty).distinct()
                .collect(Collectors.toList());
    }

    public List<Question> getRandomQuestions(String category, String difficulty, int count) {
        List<Question> filtered = filterByCriteria(category, difficulty);
        Collections.shuffle(filtered);
        return filtered.stream().limit(count).collect(Collectors.toList());
    }

    // Get N random questions from any category/difficulty (Random Trivia)
    public List<Question> getRandomMixed(int count) {
        List<Question> all = new ArrayList<>(questions);
        Collections.shuffle(all);
        return all.stream().limit(count).collect(Collectors.toList());
    }

    private void generateSampleData() {
        // GEOGRAPHY
        questions.add(new Question("1", "Geography", "Easy", "What is the capital of France?", Arrays.asList("London", "Berlin", "Paris", "Madrid"), "Paris"));
        questions.add(new Question("2", "Geography", "Easy", "Which ocean is the largest?", Arrays.asList("Atlantic", "Indian", "Arctic", "Pacific"), "Pacific"));
        questions.add(new Question("3", "Geography", "Medium", "Which country has the most natural lakes?", Arrays.asList("USA", "Canada", "Russia", "China"), "Canada"));
        questions.add(new Question("4", "Geography", "Medium", "What is the smallest country in the world?", Arrays.asList("Monaco", "Malta", "Vatican City", "San Marino"), "Vatican City"));
        questions.add(new Question("5", "Geography", "Hard", "Which African country was formerly known as Abyssinia?", Arrays.asList("Ethiopia", "Ghana", "Liberia", "Sudan"), "Ethiopia"));

        // MATH
        questions.add(new Question("6", "Math", "Easy", "What is 15 + 27?", Arrays.asList("32", "42", "41", "39"), "42"));
        questions.add(new Question("7", "Math", "Easy", "What is the square root of 64?", Arrays.asList("6", "7", "8", "9"), "8"));
        questions.add(new Question("8", "Math", "Medium", "What is 12 x 12?", Arrays.asList("124", "144", "164", "112"), "144"));
        questions.add(new Question("9", "Math", "Medium", "What is the value of Pi to two decimal places?", Arrays.asList("3.12", "3.14", "3.16", "3.18"), "3.14"));
        questions.add(new Question("10", "Math", "Hard", "What is the derivative of sin(x)?", Arrays.asList("cos(x)", "-cos(x)", "tan(x)", "sec(x)"), "cos(x)"));

        // SCIENCE
        questions.add(new Question("11", "Science", "Easy", "What planet is known as the Red Planet?", Arrays.asList("Venus", "Mars", "Jupiter", "Saturn"), "Mars"));
        questions.add(new Question("12", "Science", "Easy", "What is the chemical symbol for Water?", Arrays.asList("O2", "CO2", "H2O", "HO2"), "H2O"));
        questions.add(new Question("13", "Science", "Medium", "Which element has the atomic number 1?", Arrays.asList("Helium", "Oxygen", "Hydrogen", "Carbon"), "Hydrogen"));
        questions.add(new Question("14", "Science", "Medium", "What is the hardest natural substance on Earth?", Arrays.asList("Gold", "Iron", "Diamond", "Quartz"), "Diamond"));
        questions.add(new Question("15", "Science", "Hard", "What is the speed of light in a vacuum (approx)?", Arrays.asList("150,000 km/s", "300,000 km/s", "450,000 km/s", "600,000 km/s"), "300,000 km/s"));

        // MIXED/GENERAL
        questions.add(new Question("16", "History", "Easy", "Who discovered America in 1492?", Arrays.asList("Vasco da Gama", "Columbus", "Magellan", "Cook"), "Columbus"));
        questions.add(new Question("17", "History", "Medium", "In which year did World War II end?", Arrays.asList("1943", "1944", "1945", "1946"), "1945"));
        questions.add(new Question("18", "Science", "Hard", "Which gas makes up 78% of Earth's atmosphere?", Arrays.asList("Oxygen", "Nitrogen", "Carbon Dioxide", "Argon"), "Nitrogen"));
        questions.add(new Question("19", "Math", "Hard", "Solve for x: 2x + 5 = 15", Arrays.asList("2", "5", "10", "7.5"), "5"));
        questions.add(new Question("20", "Geography", "Hard", "What is the longest river in the world?", Arrays.asList("Amazon", "Nile", "Yangtze", "Mississippi"), "Nile"));
    }


}