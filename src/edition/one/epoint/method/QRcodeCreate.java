package edition.one.epoint.method;

import java.util.Hashtable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRcodeCreate {
	
	public static View setQRCodeCompoment(Activity act,String cusindex, String customerid) {
		String qrString = "{\"OS\":\"Android\",\"cusindex\":\"" + cusindex + "\",\"customerid\":\"" + customerid + "\"}";
		QRCodeWriter qrcodewriter = new QRCodeWriter();
		BitMatrix bitmatrix = null;
		
		try {
			Hashtable<EncodeHintType, Object> hst = new Hashtable<EncodeHintType, Object>();
			hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			bitmatrix = qrcodewriter.encode(qrString, BarcodeFormat.QR_CODE, 1, 1, hst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		class QRCodeDrawer extends View {
			BitMatrix bitmatrix;
			Paint paint;

			public QRCodeDrawer(Context context, BitMatrix bitmatrix) {
				super(context);
				this.bitmatrix = bitmatrix;
				paint = new Paint();
			}

			@SuppressLint("DrawAllocation") @Override
			public void onDraw(Canvas c) {
				if (bitmatrix == null)
					return;
				c.drawColor(Color.WHITE);
				
				Rect bounds = c.getClipBounds();
				int w = bounds.width();
				int h = bounds.height();
				
				int imageSize = bitmatrix.getHeight();
				int blockSize = (w / imageSize);

				int top_offset = (h - imageSize * blockSize) / 2;
				int left_offset = (w - imageSize * blockSize) / 2;

				for (int i = 0; i < imageSize; i++)
					for (int j = 0; j < imageSize; j++)
						if (bitmatrix.get(i, j))
							c.drawRect(	left_offset + 		i * blockSize,	// +1 增加格子間細 
										top_offset 	+ 		j * blockSize,	// +1 增加格子間細
										left_offset + (i + 1) * blockSize, 
										top_offset 	+ (j + 1) * blockSize, 
										paint);
			}
		}
		QRCodeDrawer view = new QRCodeDrawer(act, bitmatrix);
		return view;
	}


}
