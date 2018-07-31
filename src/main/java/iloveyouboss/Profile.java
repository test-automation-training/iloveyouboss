/***
 * Excerpted from "Pragmatic Unit Testing in Java with JUnit",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/utj2 for more book information.
 ***/
package iloveyouboss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Profile {

    private Map<String, Answer> answers = new HashMap<>();
    // ...

    private int score;
    private String name;

    public Profile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void add(Answer answer) {
        answers.put(answer.getQuestionText(), answer);
    }

    public boolean matches(Criteria criteria) {
        calculateScore(criteria);

        if (anyMustMatchAnswerFailed(criteria)) {
            return false;
        }

        return isAnyAnswerMatches(criteria);
    }

    private boolean isAnyAnswerMatches(Criteria criteria) {
        boolean anyMatches = false;
        for (Criterion criterion : criteria) {
            boolean match = isAnswerMatch(criterion);
            anyMatches |= match;
        }
        return anyMatches;
    }

    private void calculateScore(Criteria criteria) {
        resetScore();

        for (Criterion criterion : criteria) {
            boolean match = isAnswerMatch(criterion);
            if (match) {
                score += criterion.getWeight().getValue();
            }
        }
    }

    private boolean anyMustMatchAnswerFailed(Criteria criteria) {
        for (Criterion criterion : criteria) {
            if (!isAnswerMatch(criterion) && criterion.getWeight() == Weight.MustMatch) {
                return true;
            }
        }
        return false;
    }

    private boolean isAnswerMatch(Criterion criterion) {
        Answer answer = answers.get(
                criterion.getAnswer().getQuestionText());
        return criterion.getWeight() == Weight.DontCare ||
                answer.match(criterion.getAnswer());
    }

    private void resetScore() {
        score = 0;
    }

    public int score() {
        return score;
    }

    public List<Answer> classicFind(Predicate<Answer> pred) {
        List<Answer> results = new ArrayList<Answer>();
        for (Answer answer : answers.values())
            if (pred.test(answer))
                results.add(answer);
        return results;
    }

    @Override
    public String toString() {
        return name;
    }

    public List<Answer> find(Predicate<Answer> pred) {
        return answers.values().stream()
                .filter(pred)
                .collect(Collectors.toList());
    }
}
