package com.doryapp.dory.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.doryapp.dory.R;
import com.doryapp.dory.activities.FirebaseUserProvider;
import com.doryapp.dory.apiCalls.SendFriendRequestCall;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

import xdroid.toaster.Toaster;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CodeFragment extends Fragment {

    private static final int BLACK = 0xFFFFFFFF;
    private static final int WHITE = 0xFF000000;
    public static final int ScanRequestCode = 49374;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_code, container, false);

        setupScanButton(rootView);
        showCode(rootView);

        return rootView;
    }

    private void showCode(ViewGroup rootView) {
        ImageView imageView = (ImageView) rootView.findViewById(R.id.codeView);
        if(imageView != null)
        {
            Bitmap bitmap = getUserQR();
            imageView.setImageBitmap(bitmap);
        }
    }

    private void setupScanButton(ViewGroup rootView) {
        Button button = (Button) rootView.findViewById(R.id.scanBtn);
        if(button != null)
        {
            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onClickButtonScan();
                }
            });
        }
    }

    private void onClickButtonScan() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            startScan();
        }
    }

    private void startScan() {
        IntentIntegrator.forFragment(this).initiateScan(IntentIntegrator.QR_CODE_TYPES);
    }


    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 555;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startScan();
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == ScanRequestCode) {
            handleCodeScanResult(resultCode, intent);
        }
    }

    private void handleCodeScanResult(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            String contents = intent.getStringExtra("SCAN_RESULT");
            Toaster.toast(contents);
            sendFriendRequest(contents);
        } else if (resultCode == RESULT_CANCELED) {
            Toaster.toast("cancel");
        }
    }

    private void sendFriendRequest(String id) {
        new SendFriendRequestCall(getActivity(), id).execute();
        Toaster.toast("Friend request to " + id + " sent!");
    }

    private Bitmap getUserQR() {
        Bitmap logo = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.logo2);
        String userId = ((FirebaseUserProvider)getActivity()).getUser().getUid();

        try {
            Bitmap bitmap = generateQRFromString(userId);
            overlayLogo(bitmap, logo);
            return bitmap;
        } catch (WriterException e) {
            Toaster.toast("Error generating the QR-Code");
        }
        return null;
    }

    private void overlayLogo(Bitmap bitmap, Bitmap logo) {

        int logoWidth = bitmap.getWidth() / 3;
        int logoHeight = bitmap.getHeight() / 3;
        int codeWidth = bitmap.getWidth();
        int codeHeight = bitmap.getHeight();

        Bitmap scaledLogo = Bitmap.createScaledBitmap(logo, logoWidth, logoHeight, false);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(scaledLogo, (float)codeWidth/2-logoWidth/2, (float)codeHeight/2-logoHeight/2, null);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    Bitmap generateQRFromString(String str) throws WriterException {
        try {
            BitMatrix result;
            int width = getMaximumWidth();
            try {
                Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, width, width, hints);
            } catch (IllegalArgumentException iae) {
                // Unsupported format
                return null;
            }
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? WHITE : BLACK;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, w, h);
            return bitmap;
        } catch (WriterException ignored) {
        }
        return null;
    }

    private int getMaximumWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return Math.min(metrics.widthPixels, metrics.heightPixels);
    }

}

