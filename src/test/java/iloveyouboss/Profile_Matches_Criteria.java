package iloveyouboss;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Profile_Matches_Criteria {
    private Profile profile;
    private Criteria criteria;
    private Question questionA;
    private Question questionB;
    private Question questionC;

    @BeforeEach
    public void setUp() {
        profile = new Profile("Test Profile");
        criteria = new Criteria();

        questionA = new BooleanQuestion(1, "Test Question A");
        questionB = new BooleanQuestion(2, "Test Question B");
        questionC = new BooleanQuestion(3, "Test Question C");

        profile.add(new Answer(questionA, Bool.TRUE));
        profile.add(new Answer(questionB, Bool.FALSE));
        profile.add(new Answer(questionC, Bool.TRUE));
    }

    @Test
    public void false_when_empty() {
        assertThat(profile.matches(criteria)).isFalse();
    }

    @Test
    public void score_is_0_when_empty() {
        profile.matches(criteria);
        assertThat(profile.score()).isEqualTo(0);
    }

    @Test
    public void false_when_must_match_not_match() {
        initializeMustMatchNotMatchCriteria();
        assertThat(profile.matches(criteria)).isFalse();
    }

    @Test
    public void score_is_sum_when_must_match_not_match() {

        initializeMustMatchNotMatchCriteria();

        int score = Weight.Important.getValue() * 2;

        profile.matches(criteria);

        assertThat(profile.score()).isEqualTo(score);
    }

    @Test
    public void true_when_some_match() {
        initializeSomeMatchCriteria();
        assertThat(profile.matches(criteria)).isTrue();
    }

    @Test
    public void score_is_sum_when_some_match() {

        initializeSomeMatchCriteria();

        int score = Weight.Important.getValue() * 2;

        profile.matches(criteria);

        assertThat(profile.score()).isEqualTo(score);
    }

    @Test
    public void false_when_no_match() {
        initializeNoMatchCriterion();
        assertThat(profile.matches(criteria)).isFalse();
    }

    @Test
    public void score_is_0_when_no_match() {

        initializeNoMatchCriterion();

        profile.matches(criteria);

        assertThat(profile.score()).isEqualTo(0);
    }

    private void initializeMustMatchNotMatchCriteria() {

        Criterion criterionA = new Criterion(new Answer(questionA, Bool.TRUE), Weight.Important);
        Criterion criterionB = new Criterion(new Answer(questionB, Bool.TRUE), Weight.MustMatch);
        Criterion criterionC = new Criterion(new Answer(questionC, Bool.TRUE), Weight.Important);

        criteria.add(criterionA);
        criteria.add(criterionB);
        criteria.add(criterionC);
    }

    private void initializeSomeMatchCriteria() {

        Criterion criterionA = new Criterion(new Answer(questionA, Bool.TRUE), Weight.Important);
        Criterion criterionB = new Criterion(new Answer(questionB, Bool.TRUE), Weight.Important);
        Criterion criterionC = new Criterion(new Answer(questionC, Bool.TRUE), Weight.Important);

        criteria.add(criterionA);
        criteria.add(criterionB);
        criteria.add(criterionC);
    }

    private void initializeNoMatchCriterion() {

        Criterion criterionA = new Criterion(new Answer(questionA, Bool.FALSE), Weight.Important);
        Criterion criterionB = new Criterion(new Answer(questionB, Bool.TRUE), Weight.Important);
        Criterion criterionC = new Criterion(new Answer(questionC, Bool.FALSE), Weight.Important);

        criteria.add(criterionA);
        criteria.add(criterionB);
        criteria.add(criterionC);
    }
}
