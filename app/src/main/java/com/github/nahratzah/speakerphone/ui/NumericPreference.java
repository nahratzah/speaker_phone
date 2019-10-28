package com.github.nahratzah.speakerphone.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.util.AttributeSet;

import com.github.nahratzah.speakerphone.R;

public class NumericPreference extends Preference {
    /**
     * Perform inflation from XML and apply a class-specific base style. This
     * constructor of Preference allows subclasses to use their own base style
     * when they are inflating. For example, a {@link CheckBoxPreference}
     * constructor calls this version of the super class constructor and
     * supplies {@code android.R.attr.checkBoxPreferenceStyle} for
     * <var>defStyleAttr</var>. This allows the theme's checkbox preference
     * style to modify all of the base preference attributes as well as the
     * {@link CheckBoxPreference} class's attributes.
     *
     * @param context      The Context this is associated with, through which it can
     *                     access the current theme, resources,
     *                     {@link SharedPreferences}, etc.
     * @param attrs        The attributes of the XML tag that is inflating the
     *                     preference.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     * @param defStyleRes  A resource identifier of a style resource that
     *                     supplies default values for the view, used only if
     *                     defStyleAttr is 0 or can not be found in the theme. Can be 0
     *                     to not look for defaults.
     * @see #NumericPreference(Context, AttributeSet)
     */
    public NumericPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setWidgetLayoutResource(R.layout.ui_numeric_preference);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style. This
     * constructor of Preference allows subclasses to use their own base style
     * when they are inflating. For example, a {@link CheckBoxPreference}
     * constructor calls this version of the super class constructor and
     * supplies {@code android.R.attr.checkBoxPreferenceStyle} for
     * <var>defStyleAttr</var>. This allows the theme's checkbox preference
     * style to modify all of the base preference attributes as well as the
     * {@link CheckBoxPreference} class's attributes.
     *
     * @param context      The Context this is associated with, through which it can
     *                     access the current theme, resources,
     *                     {@link SharedPreferences}, etc.
     * @param attrs        The attributes of the XML tag that is inflating the
     *                     preference.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     * @see #NumericPreference(Context, AttributeSet)
     */
    public NumericPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.ui_numeric_preference);
    }

    /**
     * Constructor that is called when inflating a Preference from XML. This is
     * called when a Preference is being constructed from an XML file, supplying
     * attributes that were specified in the XML file. This version uses a
     * default style of 0, so the only attribute values applied are those in the
     * Context's Theme and the given AttributeSet.
     *
     * @param context The Context this is associated with, through which it can
     *                access the current theme, resources, {@link SharedPreferences},
     *                etc.
     * @param attrs   The attributes of the XML tag that is inflating the
     *                preference.
     * @see #NumericPreference(Context, AttributeSet, int)
     */
    public NumericPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.ui_numeric_preference);
    }

    /**
     * Constructor to create a Preference.
     *
     * @param context The Context in which to store Preference values.
     */
    public NumericPreference(Context context) {
        super(context);
        setWidgetLayoutResource(R.layout.ui_numeric_preference);
    }
}
