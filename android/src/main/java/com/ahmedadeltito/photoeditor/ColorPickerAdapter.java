package com.ahmedadeltito.photoeditor;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ui.photoeditor.R;

/**
 * Created by Ahmed Adel on 5/8/17.
 */

class CheckIconDrawable extends Drawable {

    private final Paint paint;
    private final int size;

    public CheckIconDrawable(int color, int size) {
        this.size = size;
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int width = getBounds().width();
        int height = getBounds().height();

        Path path = new Path();
        path.moveTo(width * 0.3f, height * 0.5f);
        path.lineTo(width * 0.45f, height * 0.7f);
        path.lineTo(width * 0.75f, height * 0.4f);

        canvas.drawPath(path, paint);
    }

    @Override
    public int getIntrinsicWidth() {
        return size;
    }

    @Override
    public int getIntrinsicHeight() {
        return size;
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}

public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Integer> colorPickerColors;
    private OnColorPickerClickListener onColorPickerClickListener;
    private int selectedPosition = 0;

    public ColorPickerAdapter(@NonNull Context context, @NonNull List<Integer> colorPickerColors) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.colorPickerColors = colorPickerColors;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.color_picker_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        boolean isSelected = position == selectedPosition; // replace with your selection logic
        buildColorPickerView(holder.colorPickerView, colorPickerColors.get(position), isSelected);
    }

    @Override
    public int getItemCount() {
        return colorPickerColors.size();
    }

    private ShapeDrawable createShapeDrawable(String shapeType, int size, int color) {
        ShapeDrawable shapeDrawable;

        if ("diamond".equals(shapeType)) {
            Path path = new Path();
            path.moveTo(size / 2f, 0);
            path.lineTo(size, size / 2f);
            path.lineTo(size / 2f, size);
            path.lineTo(0, size / 2f);
            path.close();

            shapeDrawable = new ShapeDrawable(new PathShape(path, size, size));
        } else {
            shapeDrawable = new ShapeDrawable(new OvalShape());
        }

        shapeDrawable.setIntrinsicHeight(size);
        shapeDrawable.setIntrinsicWidth(size);
        shapeDrawable.setBounds(new Rect(0, 0, size, size));
        shapeDrawable.getPaint().setColor(color);

        return shapeDrawable;
    }

    private void buildColorPickerView(View view, int colorCode, boolean isSelected) {
        view.setVisibility(View.VISIBLE);
        ShapeDrawable backgroundShape = createShapeDrawable("circle", 30, Color.WHITE);
        // smallerShape.setPadding(10, 10, 10, 10);

        Drawable[] drawables;
        ShapeDrawable biggerShape = createShapeDrawable("circle", 20, colorCode);

        if (isSelected) {
            Drawable checkIcon = new CheckIconDrawable(Color.WHITE, 20); // 20 is the size of the check icon
            drawables = new Drawable[] { backgroundShape, biggerShape, checkIcon };

            LayerDrawable layerDrawable = new LayerDrawable(drawables);
            int centerX = (biggerShape.getIntrinsicWidth() - checkIcon.getIntrinsicWidth()) / 2;
            int centerY = (biggerShape.getIntrinsicHeight() - checkIcon.getIntrinsicHeight()) / 2;

            layerDrawable.setLayerInset(2, centerX, centerY, centerX, centerY); // 2 is the index of checkIcon in the drawables array
            view.setBackgroundDrawable(layerDrawable);
        } else {
            view.setBackgroundDrawable(
                new LayerDrawable(new Drawable[] { backgroundShape, biggerShape })
            );
        }
    }

    public void setOnColorPickerClickListener(
        OnColorPickerClickListener onColorPickerClickListener
    ) {
        this.onColorPickerClickListener = onColorPickerClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View colorPickerView;

        public ViewHolder(View itemView) {
            super(itemView);
            colorPickerView = itemView.findViewById(R.id.color_picker_view);
            itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (
                            onColorPickerClickListener != null
                        ) onColorPickerClickListener.onColorPickerClickListener(
                            colorPickerColors.get(getAdapterPosition())
                        );
                        selectedPosition = getAdapterPosition();
                        notifyDataSetChanged();
                    }
                }
            );
        }
    }

    public interface OnColorPickerClickListener {
        void onColorPickerClickListener(int colorCode);
    }
}
