
package dev.answer.scanqr;

import android.*;
import android.app.*;
import android.os.*;
import android.os.Bundle;
import android.view.*;
import androidx.appcompat.app.AppCompatActivity;
import com.blankj.utilcode.util.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import dev.answer.scanqr.databinding.ActivityMainBinding;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private BeepManager beepManager;
     public BarcodeCallback callback = new BarcodeCallback(){
         @Override
         public void barcodeResult(BarcodeResult result) {
             // TODO: Implement this method
            if (result.getText() == null ) {
                // Prevent duplicate scans
                return;
            }
            binding.barcodeScanner.setStatusText(result.getText());

            beepManager.playBeepSoundAndVibrate();

         }
     };
    
    public void initView() {
        //设置屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //初始化扫码
        var formats  =
            List.of(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        binding.barcodeScanner.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        binding.barcodeScanner.initializeFromIntent(getIntent());
        binding.barcodeScanner.decodeContinuous(callback);

        beepManager = new BeepManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        gotoScan();
        
        // Inflate and get instance of binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // set content view to binding's root
        setContentView(binding.getRoot());
        
        initView();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // TODO: Implement this method
        binding.barcodeScanner.resume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // TODO: Implement this method
        binding.barcodeScanner.pause();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO: Implement this method
        return binding.barcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
    
    
    public void gotoScan(){
		PermissionUtils.permission(Manifest.permission.CAMERA)
			.callback(new PermissionUtils.SimpleCallback(){

				@Override
				public void onDenied()
				{
					// TODO: Implement this method
					LogUtils.e("没有相机权限，无法扫码，请在设置中开启");
                    ToastUtils.showShort("没有相机权限，无法扫码，请在设置中开启");
				}

				@Override
				public void onGranted()
				{
					// TODO: Implement this method
					//相机权限已开启
                    LogUtils.i("相机权限已开启");
				}
			}).request();	
	}
}
