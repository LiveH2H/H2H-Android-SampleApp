package itutorgroup.h2h.utils;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import itutorgroup.h2h.R;


public class ViewUtil {

    private static final String TAG_STATUSBAR = "statusBar";

    public static <T> T findViewById(View v, int id) {
        //noinspection unchecked
        return (T) v.findViewById(id);
    }

    public static <T> T findViewById(Activity activity, int id) {
        //noinspection unchecked
        return (T) activity.findViewById(id);
    }

	/*public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
						editText.setText(s);
						editText.setSelection(s.length());
						return;
					}
				}
				if (s.toString().startsWith(".")) {
					s = "0.";
					editText.setText(s);
					editText.setSelection(2);
					return;
				}
				if (s.toString().startsWith("0") && s.length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						editText.setText("0");
						editText.setSelection(1);
					}
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}*/

    /**
     * 为editText设置分隔符 123-232-123
     *
     * @param editText
     */
    public static void setSeparator(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && s.length() % 4 == 0) {
                    s = StringUtil.space(s.toString(), "-", 3);
                    editText.setText(s.toString());
                    editText.setSelection(s.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 该方法使api 19以上的设备状态栏颜色改变，在setContentView后调用
     *
     * 注意：状态栏的颜色可以在主题的status_bar_bg里面设置，默认为黑色
     *
     * @param activity
     */
    public static void initStatusBar(Activity activity) {
        // 获取状态栏背景色配置
        TypedArray array = activity.getTheme().obtainStyledAttributes(new int[]{R.attr.status_bar_bg});
        int statusBarColor = array.getColor(0, Color.TRANSPARENT);
        array.recycle();

        // 设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(statusBarColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 获取系统状态栏高度
            int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId <= 0) {
                return;
            }
            int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            int count = decorView.getChildCount();
            if (count > 0) {
                View statusBarView = decorView.getChildAt(count - 1);
                String tag = (String) statusBarView.getTag();
                // 避免重复调用
                if(TAG_STATUSBAR.equals(tag)){
                    statusBarView.setBackgroundColor(statusBarColor);
                    return;
                }
            }

            View statusBarView = new View(activity);
            statusBarView.setTag(TAG_STATUSBAR);
            ViewGroup.LayoutParams params =
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            statusBarView.setLayoutParams(params);
            statusBarView.setBackgroundColor(statusBarColor);
            decorView.addView(statusBarView);

            // 设置根布局参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            if(rootView != null){
                rootView.setFitsSystemWindows(true);
                rootView.setClipToPadding(true);
            }
        }
    }

    /**
     * 设置标题栏高度，把状态栏包括进来
     * @param mContext
     * @param view
     * @param barHeight
     */
    /*public static void setBarHeight(Context mContext, View view, int barHeight) {
		if (android.os.Build.VERSION.SDK_INT >= 19) {
			int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
			if (resourceId > 0) {
				int statusBarHeight = mContext.getResources().getDimensionPixelSize(resourceId);
				LayoutParams layoutParams = view.getLayoutParams();
				layoutParams.height = statusBarHeight + barHeight;
				view.setPadding(0, statusBarHeight, 0, 0);
			}
		}
	}*/

    /**
     * 设置标题栏高度，把状态栏包括进来
     * @param mContext
     * @param view
     */
	/*public static void setBarHeight(Context mContext, View view) {
		if (android.os.Build.VERSION.SDK_INT >= 19) {
			int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
			if (resourceId > 0) {
				int statusBarHeight = mContext.getResources().getDimensionPixelSize(resourceId);
				view.setPadding(view.getPaddingLeft(),
						statusBarHeight + view.getPaddingTop(),
						view.getPaddingRight(), view.getPaddingBottom());
			}
		}
	}*/

    /**
     * 根据EditText是否有内容，显示label
     *
     * @param editText
     * @param label
     */
    public static void setEdittextLabelVisibility(EditText editText, final View label) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setVisibility(label, s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    /**
     * 设置输入框内容变化，显示或隐藏label和clear Button
     *
     * @param editText
     * @param button
     * @param label
     */
    public static void setEdittextChangeVisibilityClearButton(final EditText editText, final View button, final View label) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setVisibility(label, s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
                if (s.length() > 0 && editText.isFocused()) {
                    setVisibility(button, View.VISIBLE);
                } else {
                    setVisibility(button, View.INVISIBLE);
                }
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (editText.getText().length() > 0 && hasFocus) {
                    setVisibility(button, View.VISIBLE);
                } else {
                    setVisibility(button, View.INVISIBLE);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(null);
            }
        });
    }

    /**
     * 设置View显示或隐藏
     *
     * @param view
     * @param visibility
     */
    public static void setVisibility(View view, int visibility) {
        if (view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    /**
     * 设置密码输入框显示或隐藏
     *
     * @param editText
     * @param tvShow
     */
    public static void setPasswordShow(final EditText editText, final TextView tvShow, final TextView tvLabel) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    setVisibility(tvLabel, View.VISIBLE);
                    setVisibility(tvShow, View.VISIBLE);
                } else {
                    setVisibility(tvLabel, View.INVISIBLE);
                    setVisibility(tvShow, View.INVISIBLE);
                }
            }
        });
        tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isShow = (String) tvShow.getTag();
                if (isShow.equals("false")) {
                    tvShow.setText(R.string.password_hide);
                    tvShow.setTag("true");
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    tvShow.setText(R.string.password_show);
                    tvShow.setTag("false");
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    setPasswordFontDefault(editText);
                }
                editText.setSelection(editText.length());
            }
        });
    }

    /**
     * 设置输入法直接触发点击事件
     *
     * @param editText
     * @param target
     */
    public static void setEditorAction(EditText editText, final View target) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    target.performClick();
                }
                return false;
            }
        });
    }

    /**
     * 设置背景
     * @param view
     * @param drawable
     */
    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 点击监听
     *
     * @param id
     * @param onClickListener
     */
    public static void setOnClickListener(Activity context, @IdRes int id, View.OnClickListener onClickListener) {
        View view = context.findViewById(id);
        if(view != null){
            view.setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置密码输入框字体同其它文本输入框
     * @param editText
     */
    public static void setPasswordFontDefault(EditText editText) {
        editText.setTypeface(Typeface.DEFAULT);
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    /**
     * 限制输入的字符,只能文字,数字和空格,且不能以空格开头
     * @param editText
     */
    public static void setFiltersName(EditText editText) {
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (dstart == 0 && i == 0 && Character.isSpaceChar(c)) {
                        return "";
                    }
                    if (!Character.isLetterOrDigit(c) && !Character.isSpaceChar(c)) {
                        return "";
                    }
                }
                return null;
            }
        }});
    }

    /**
     * 限制输入字母和数字,且不能以数字开头
     * @param editText
     */
    public static void setFiltersExtrenalId(EditText editText) {
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (dstart == 0 && i == 0 && Character.isDigit(c)) {
                        return "";
                    }
                    if (!Character.isLowerCase(c) && !Character.isUpperCase(c) && !Character.isDigit(c)) {
                        return "";
                    }
                }
                return null;
            }
        }});
    }

    /**
     * 限制输入的数字,且为phone number
     * @param editText
     */
    public static void setFiltersPhoneNumber(final EditText editText) {
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (dstart == 0 && i == 0 && c != '1') {
                        return "";
                    }
//                    if (!Character.isDigit(c)) {
//                        return "";
//                    }
                }
                return null;
            }
        }});
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 11) {
                    editText.setText(s.toString().substring(0, 11));
                    editText.setSelection(editText.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 限制输入字母和数字,且不能以数字开头
     * @param editText
     */
    public static void setFiltersEmail(EditText editText) {
        editText.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (!Character.isLowerCase(c) && !Character.isUpperCase(c) && !Character.isDigit(c)
                            && c != '.' && c != '+' && c != '-' && c != '_' && c != '@') {
                        return "";
                    }
                }
                return null;
            }
        }});
    }

}
