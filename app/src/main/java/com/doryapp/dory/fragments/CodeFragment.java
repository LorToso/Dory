package com.doryapp.dory.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doryapp.dory.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class CodeFragment extends Fragment {

    private static final int BLACK = 0xFFFFFFFF;
    private static final int WHITE = 0xFF000000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_code, container, false);


        ImageView imageView = (ImageView) rootView.findViewById(R.id.codeView);
        if(imageView != null)
        {
            Bitmap bitmap = getFriendshipCode();
            imageView.setImageBitmap(bitmap);
        }

        return rootView;
    }

    private Bitmap getFriendshipCode() {
        Bitmap logo = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.logo);

        try {
            Bitmap bitmap = generateQRFromString("testtesttesttesttest");
            overlayLogo(bitmap, logo);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void overlayLogo(Bitmap bitmap, Bitmap logo) {

        int logoWidth = bitmap.getWidth()/3;
        int logoHeight = bitmap.getHeight() / 3;

        Bitmap scaledLogo = Bitmap.createScaledBitmap(logo, logoWidth, logoHeight, false);

        int[] pixels = new int[scaledLogo.getWidth() * scaledLogo.getHeight()];
        scaledLogo.getPixels(pixels, 0, logoWidth, 0, 0, logoWidth, logoHeight);
        bitmap.setPixels(pixels, 0, logoWidth, logoWidth, logoHeight, logoWidth, logoHeight);
    }

    Bitmap generateQRFromString(String str) throws WriterException {
        try {
            BitMatrix result;
            int width = getMaximumWidth();
            int height = width;
            try {
                result = new MultiFormatWriter().encode(str,
                        BarcodeFormat.QR_CODE, width, height, null);
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
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
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

