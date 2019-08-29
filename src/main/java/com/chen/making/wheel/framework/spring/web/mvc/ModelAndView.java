package com.chen.making.wheel.framework.spring.web.mvc;

import com.chen.making.wheel.framework.spring.http.HttpStatus;
import lombok.Data;

import java.util.Map;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
@Data
public class ModelAndView {

    private Object view;

    /** Model Map */
    private Map<String, Object> model;

    private HttpStatus status;

    public ModelAndView(Object view, HttpStatus httpStatus) {
        this(view, null, httpStatus);
    }

    public ModelAndView(Object view, Map<String, Object> model) {
        this(view, model, HttpStatus.OK);
    }

    public ModelAndView(Object view, Map<String, Object> model, HttpStatus status) {
        this.view = view;
        this.model = model;
        this.status = status;
    }

    /**
     * Set a view name for this ModelAndView, to be resolved by the
     * DispatcherServlet via a ViewResolver. Will override any
     * pre-existing view name or View.
     */
    public void setViewName(String viewName) {
        this.view = viewName;
    }

    /**
     * Return the view name to be resolved by the DispatcherServlet
     * via a ViewResolver, or {@code null} if we are using a View object.
     */
    public String getViewName() {
        return (this.view instanceof String ? (String) this.view : null);
    }

    /**
     * Set a View object for this ModelAndView. Will override any
     * pre-existing view name or View.
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Return the View object, or {@code null} if we are using a view name
     * to be resolved by the DispatcherServlet via a ViewResolver.
     */
    public View getView() {
        return (this.view instanceof View ? (View) this.view : null);
    }

    /**
     * Indicate whether or not this {@code ModelAndView} has a view, either
     * as a view name or as a direct {@link View} instance.
     */
    public boolean hasView() {
        return (this.view != null);
    }

    /**
     * Return whether we use a view reference, i.e. {@code true}
     * if the view has been specified via a name to be resolved by the
     * DispatcherServlet via a ViewResolver.
     */
    public boolean isReference() {
        return (this.view instanceof String);
    }

    /**
     * Return the model map. May return {@code null}.
     * Called by DispatcherServlet for evaluation of the model.
     */
    public Map<String, Object> getModelInternal() {
        return this.model;
    }

}
