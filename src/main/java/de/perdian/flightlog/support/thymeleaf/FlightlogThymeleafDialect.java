package de.perdian.flightlog.support.thymeleaf;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.IPostProcessorDialect;
import org.thymeleaf.engine.AbstractTemplateHandler;
import org.thymeleaf.model.ICloseElementTag;
import org.thymeleaf.model.IOpenElementTag;
import org.thymeleaf.model.IText;
import org.thymeleaf.postprocessor.IPostProcessor;
import org.thymeleaf.postprocessor.PostProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class FlightlogThymeleafDialect implements IPostProcessorDialect {

    @Override
    public int getDialectPostProcessorPrecedence() {
        return 0;
    }

    @Override
    public String getName() {
        return "flightlog";
    }

    @Override
    public Set<IPostProcessor> getPostProcessors() {
        return Set.of(
            new PostProcessor(TemplateMode.HTML, RemoveWhitespacesTemplateHandler.class, 0)
        );
    }

    public static class RemoveWhitespacesTemplateHandler extends AbstractTemplateHandler {

        private List<ElementInfo> elementStack = new LinkedList<>();
        private List<String> ignoreElementNames = List.of("html", "head", "body", "span", "pre");
        private List<String> trimElementNames = List.of("a", "button");

        @Override
        public void handleOpenElement(IOpenElementTag openElementTag) {
            ElementInfo newElementInfo = new ElementInfo(openElementTag.getElementDefinition().getElementName().getElementName().toLowerCase());
            ElementInfo currentElementInfo = this.getElementStack().isEmpty() ? null : this.getElementStack().getLast();
            if (currentElementInfo != null) {
                currentElementInfo.addChild(newElementInfo);
            }
            this.getElementStack().addLast(newElementInfo);
            super.handleOpenElement(openElementTag);
        }

        @Override
        public void handleCloseElement(ICloseElementTag closeElementTag) {
            if (!this.getElementStack().isEmpty()) {
                this.getElementStack().removeLast();
            }
            super.handleCloseElement(closeElementTag);
        }

        @Override
        public void handleText(IText text) {
            ElementInfo currentElementInfo = this.getElementStack().isEmpty() ? null : this.getElementStack().getLast();
            if (currentElementInfo == null || this.getIgnoreElementNames().contains(currentElementInfo.getElementName())) {
                super.handleText(text);
            } else {
                String currentTextValue = text.getText();
                if (StringUtils.isBlank(currentTextValue)) {
                    // Ignore
                } else {
                    String reducedTextValue = this.reduceString(currentTextValue, currentElementInfo);
                    if (!Objects.equals(currentTextValue, reducedTextValue)) {
                        super.handleText(this.getContext().getModelFactory().createText(reducedTextValue));
                    } else {
                        super.handleText(text);
                    }
                }
            }
        }

        private String reduceString(String inputString, ElementInfo elementInfo) {
            if (this.getTrimElementNames().contains(elementInfo.getElementName())) {
                if (!elementInfo.getChildren().isEmpty()) {
                    return inputString;
                } else {
                    return inputString.strip();
                }
            } else {
                return inputString;
            }
        }

        List<ElementInfo> getElementStack() {
            return this.elementStack;
        }
        void setElementStack(List<ElementInfo> elementStack) {
            this.elementStack = elementStack;
        }

        List<String> getIgnoreElementNames() {
            return this.ignoreElementNames;
        }
        void setIgnoreElementNames(List<String> ignoreElementNames) {
            this.ignoreElementNames = ignoreElementNames;
        }

        List<String> getTrimElementNames() {
            return this.trimElementNames;
        }
        void setTrimElementNames(List<String> trimElementNames) {
            this.trimElementNames = trimElementNames;
        }

        static class ElementInfo {

            private String elementName = null;
            private boolean nonWhitespaceTextAdded = false;
            private List<ElementInfo> children = new LinkedList<>();

            private ElementInfo(String elementName) {
                this.setElementName(elementName);
            }

            @Override
            public String toString() {
                return this.getElementName();
            }

            String getElementName() {
                return this.elementName;
            }
            private void setElementName(String elementName) {
                this.elementName = elementName;
            }

            boolean isNonWhitespaceTextAdded() {
                return this.nonWhitespaceTextAdded;
            }
            void setNonWhitespaceTextAdded(boolean nonWhitespaceTextAdded) {
                this.nonWhitespaceTextAdded = nonWhitespaceTextAdded;
            }

            void addChild(ElementInfo child) {
                this.getChildren().add(child);
            }
            List<ElementInfo> getChildren() {
                return this.children;
            }
            void setChildren(List<ElementInfo> children) {
                this.children = children;
            }

        }

    }

}
