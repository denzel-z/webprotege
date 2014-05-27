package edu.stanford.bmir.protege.web.client.ui.termbuilder.recommend;

import org.semanticweb.owlapi.model.OWLClass;

import java.io.Serializable;

public class ClassStringAndSuperClassPair implements Serializable {
    String className;
    OWLClass superClass;

    public ClassStringAndSuperClassPair(String className, OWLClass superClass) {
        this.className = className;
        this.superClass = superClass;
    }

    public ClassStringAndSuperClassPair() {
    }

    public String getClassName() {
        return className;
    }

    public OWLClass getSuperClass() {
        return superClass;
    }
}
