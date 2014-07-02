package edu.stanford.bmir.protege.web.shared.termbuilder;

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
