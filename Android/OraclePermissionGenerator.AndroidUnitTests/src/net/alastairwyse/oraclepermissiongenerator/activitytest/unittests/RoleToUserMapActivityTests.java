package net.alastairwyse.oraclepermissiongenerator.activitytest.unittests;

import android.content.Context;
import android.content.Intent;
import android.test.*
;
import android.view.WindowManager.BadTokenException;
import net.alastairwyse.oraclepermissiongenerator.activitytest.*;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.ILocationProvider;
import net.alastairwyse.oraclepermissiongenerator.datainterfacelayer.IRemoteDataModelProxy;
import static org.mockito.Mockito.*;

/**
 * Unit tests for class oraclepermissiongenerator.activitytest.RoleToUserMapActivity.
 * @author Alastair Wyse
 */
public class RoleToUserMapActivityTests extends ActivityUnitTestCase<RoleToUserMapActivity> {

    private Context mockContext;
    private RoleToUserMapActivity testRoleToUserMapActivity;
    
    public RoleToUserMapActivityTests() {
        super(RoleToUserMapActivity.class);
        
        mockContext = mock(Context.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        try {
            final Intent launchIntent = new Intent(getInstrumentation().getTargetContext(), RoleToUserMapActivity.class);
            startActivity(launchIntent, null, null);
            super.setActivityContext(mockContext);
            testRoleToUserMapActivity = getActivity();
        }
        // Need to catch and throw away a BadTokenException, which occurs when a dialog window is opened as part of the activity onCreate() method.
        //   This is know bug with the ActivityUnitTestCase class... https://code.google.com/p/android/issues/detail?id=14616
        catch (BadTokenException e) {}
    }

    public void testDoesSetupWork () throws Exception {
    }
    
    public void testOnDestroy() throws Throwable {
        /*
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                testRoleToUserMapActivity.onDestroy();
            }
        });
        */
    }
}
