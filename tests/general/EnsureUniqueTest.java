package general;

import java.security.SecureRandom;
import javax.crypto.spec.IvParameterSpec;
import org.checkerframework.checker.linear.qual.Unique;

class EnsureUniqueTest {
    // For example, suppose state0 is a initial state and will be changed
    // to state1 after being called by test1, while test 2 only accepts
    // state0.
    // One question is should it be only used on method parameter?
    // multi args mean all allowed
    // change the postcondition into @top and try to show some msg.
    // relation between top and mayalias
    /*
    states = {"", "initialized", "used"}
    * */
    public void test1(byte @Unique [] bytes) {
        byte @Unique({}) [] bytesIV = bytes;
        SecureRandom secureRandom = new SecureRandom();
        // After this method call,  byte @Unique [] bytesIV becomes  byte @Unique("initialized") []
        secureRandom.nextBytes(bytesIV);
        byte @Unique({}) [] newBytesIv;
        // transfer state and the rhs becomes disappear
        newBytesIv = bytesIV;
        // ::error: unique.assignment.not.allowed
        newBytesIv = bytesIV;
        // newBytesIv becomes @Unique({"used"}) ,not finished yet!
        // TODO: try debug options, write a local simulating class first
        IvParameterSpec ivSpec = new IvParameterSpec(newBytesIv);
        byte @Unique({}) [] testBytesIv;
        testBytesIv = newBytesIv;
    }
}
