/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */

package net.sourceforge.jeuclid.elements.presentation.token;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.text.AttributedString;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.support.text.StringUtil;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;
import net.sourceforge.jeuclid.layout.TextObject;

import org.w3c.dom.mathml.MathMLPresentationToken;

/**
 * Common functionality for all tokens based on a text layout.
 * 
 * @version $Revision$
 */
public abstract class AbstractTokenWithTextLayout extends
        AbstractJEuclidElement implements MathMLPresentationToken {

    /**
     * Default constructor.
     */
    public AbstractTokenWithTextLayout() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void layoutStageInvariant(final LayoutView view,
            final LayoutInfo info, final LayoutStage stage,
            final LayoutContext context) {
        if (!this.isEmpty()) {
            final Graphics2D g = view.getGraphics();
            final TextLayout t = this.produceTextLayout(g, context);
            final StringUtil.TextLayoutInfo tli = StringUtil
                    .getTextLayoutInfo(t, false);
            info.setAscentHeight(tli.getAscent(), stage);
            info.setDescentHeight(tli.getDescent(), stage);
            final float width = tli.getWidth();
            info.setHorizontalCenterOffset(width / 2.0f, stage);
            info.setWidth(width, stage);
            info.setGraphicsObject(new TextObject(t, tli.getOffset(),
                    (Color) this.applyLocalAttributesToContext(context)
                            .getParameter(Parameter.MATHCOLOR)));
        }
    }

    /**
     * Retrieve the text content as attributed string. Should be overridden
     * 
     * @param context
     *            Current Layout Context
     * @return an AttributedString
     */
    protected abstract AttributedString textContentAsAttributedString(
            LayoutContext context);

    /**
     * Checks if this element is empty.
     * 
     * @return true if empty.
     */
    protected abstract boolean isEmpty();

    private TextLayout produceTextLayout(final Graphics2D g2d,
            final LayoutContext context) {
        TextLayout layout;

        final LayoutContext now = this.applyLocalAttributesToContext(context);
        layout = StringUtil.createTextLayoutFromAttributedString(g2d, this
                .textContentAsAttributedString(now), now);
        // final Rectangle2D r2d = this.layout.getBounds();
        // final float xo = (float) r2d.getX();
        // if (xo < 0) {
        // this.xOffset = -xo;
        // } else {
        // this.xOffset = 0.0f;
        // }
        return layout;
    }

}
