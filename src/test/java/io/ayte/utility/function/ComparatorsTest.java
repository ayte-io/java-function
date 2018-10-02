package io.ayte.utility.function;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ComparatorsTest {
    @Test
    public void intrinsicVerification() {
        val subject = Arrays.asList(2, 3, 1);
        subject.sort(Comparators.intrinsic());
        assertThat(subject, equalTo(Arrays.asList(1, 2, 3)));
    }
}
