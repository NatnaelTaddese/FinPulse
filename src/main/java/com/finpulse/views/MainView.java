package com.finpulse.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@Route("")
@PageTitle("FinPulse")
@AnonymousAllowed
public class MainView extends VerticalLayout {

    public MainView() {
        addClassName("landing-page");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        add(
            createNavBar(),
            createHeroSection(),
            createFeaturesSection(),
            createStatisticsSection(),
            createTestimonialsSection(),
            createCtaSection(),
            createFooter()
        );
    }

    private HorizontalLayout createNavBar() {
        HorizontalLayout navbar = new HorizontalLayout();
        navbar.addClassName("glass-navbar");

        H2 logo = new H2("FinPulse");
        logo.addClassName("nav-logo");

        HorizontalLayout navLinks = new HorizontalLayout();
        navLinks.add(
            new Anchor("#features", "Features"),
            new Anchor("#about", "About"),
            new Anchor("#testimonials", "Testimonials")
        );
        navLinks.addClassName("nav-links");

        HorizontalLayout authButtons = new HorizontalLayout();
        Button loginButton = new Button("Login", e -> getUI().ifPresent(ui -> ui.navigate("login")));
        Button signupButton = new Button("Sign Up", e -> getUI().ifPresent(ui -> ui.navigate("signup")));
        loginButton.addClassName("nav-login-btn");
        signupButton.addClassName("nav-signup-btn");
        authButtons.add(loginButton, signupButton);
        authButtons.addClassName("nav-auth-buttons");

        navbar.add(logo, navLinks, authButtons);
        return navbar;
    }

    private Div createHeroSection() {
        Div hero = new Div();
        hero.addClassName("hero-section");

        VerticalLayout heroContent = new VerticalLayout();
        heroContent.addClassName("hero-content");

        H1 title = new H1("Master Your Money, Shape Your Future");
        title.addClassName("hero-title");

        Paragraph motto = new Paragraph("Where Financial Intelligence Meets Personal Growth");
        motto.addClassName("hero-motto");

        Paragraph subtitle = new Paragraph(
            "Transform your financial journey with FinPulse's intelligent tracking, " +
            "real-time insights, and personalized guidance."
        );
        subtitle.addClassName("hero-subtitle");

        Button ctaButton = new Button("Start Your Journey", e -> getUI().ifPresent(ui -> ui.navigate("signup")));
        ctaButton.addClassName("hero-cta-btn");

        heroContent.add(title, motto, subtitle, ctaButton);
        hero.add(heroContent);
        return hero;
    }

    private Div createFeaturesSection() {
        Div features = new Div();
        features.addClassName("features-section");
    
        H2 title = new H2("Why Choose FinPulse?");
        title.addClassName("section-title");
    
        HorizontalLayout cards = new HorizontalLayout();
        cards.addClassName("feature-cards");
    
        cards.add(
            createFeatureCard(
                new SvgIcon("icons/chart-line.svg"),
                "Smart Analytics",
                "Advanced tracking and insights to help you understand your spending patterns"
            ),
            createFeatureCard(
                new SvgIcon("icons/save.svg"),
                "Goal Setting",
                "Set and achieve your financial goals with personalized recommendations"
            ),
            createFeatureCard(
                new SvgIcon("/icons/shield-check.svg"),
                "Secure Integration",
                "Seamlessly connect with Alipay for real-time transaction monitoring"
            )
        );
    
        features.add(title, cards);
        return features;
    }
    
    private Div createStatisticsSection() {
        Div stats = new Div();
        stats.addClassName("statistics-section");
    
        HorizontalLayout counters = new HorizontalLayout();
        counters.addClassName("stat-counters");
    
        counters.add(
            createStatCounter("5", "Active Users"),
            createStatCounter("¥1000+", "Tracked Monthly"),
            createStatCounter("98%", "User Satisfaction")
        );
    
        stats.add(counters);
        return stats;
    }
    
    private Div createTestimonialsSection() {
        Div testimonials = new Div();
        testimonials.addClassName("testimonials-section");
    
        H2 title = new H2("What Our Users Say");
        title.addClassName("section-title");
    
        HorizontalLayout cards = new HorizontalLayout();
        cards.addClassName("testimonial-cards");
    
        cards.add(
            createTestimonialCard(
                "Sarah Chen",
                "Student",
                "FinPulse helped me manage my university expenses effectively!",
                5
            ),
            createTestimonialCard(
                "David Wang",
                "Professional",
                "The best financial tracking app I've ever used.",
                5
            )
        );
    
        testimonials.add(title, cards);
        return testimonials;
    }
    

    private Div createFeatureCard(SvgIcon icon, String title, String description) {
        Div card = new Div();
        card.addClassName("feature-card");


        icon.addClassName("feature-icon");

        H3 featureTitle = new H3(title);
        featureTitle.addClassName("feature-title");

        Paragraph featureDesc = new Paragraph(description);
        featureDesc.addClassName("feature-description");

        card.add(icon, featureTitle, featureDesc);
        return card;
    }


    private Div createCtaSection() {
        Div cta = new Div();
        cta.addClassName("cta-section");

        H2 title = new H2("Start Your Financial Journey Today");
        title.addClassName("cta-title");

        Paragraph subtitle = new Paragraph("Join thousands of users who have transformed their financial habits with FinPulse");
        subtitle.addClassName("cta-subtitle");

        Button startButton = new Button("Get Started Free", e -> getUI().ifPresent(ui -> ui.navigate("signup")));
        startButton.addClassName("cta-button");

        cta.add(title, subtitle, startButton);
        return cta;
    }

    private Footer createFooter() {
        Footer footer = new Footer();
        footer.addClassName("footer");

        Div footerContent = new Div();
        footerContent.addClassName("footer-content");

        // Company Info
        Div companyInfo = new Div();
        companyInfo.addClassName("footer-section");
        companyInfo.add(
            new H3("FinPulse"),
            new Paragraph("Your Intelligent Financial Companion")
        );

        // Links
        Div links = new Div();
        links.addClassName("footer-section");
        links.add(
            new H4("Quick Links"),
            new UnorderedList(
                new ListItem(new Anchor("#", "Features")),
                new ListItem(new Anchor("#", "About")),
                new ListItem(new Anchor("#", "Contact"))
            )
        );

        // Contact
        Div contact = new Div();
        contact.addClassName("footer-section");
        contact.add(
            new H4("Contact"),
            new Paragraph("support@finpulse.com"),
            new Paragraph("+86 123 456 7890")
        );

        footerContent.add(companyInfo, links, contact);
        footer.add(footerContent);

        return footer;
    }

    private Div createStatCounter(String number, String label) {
        Div counter = new Div();
        counter.addClassName("stat-counter");

        H3 statNumber = new H3(number);
        statNumber.addClassName("stat-number");

        Paragraph statLabel = new Paragraph(label);
        statLabel.addClassName("stat-label");

        counter.add(statNumber, statLabel);
        return counter;
    }

    private Div createTestimonialCard(String name, String role, String quote, int rating) {
        Div card = new Div();
        card.addClassName("testimonial-card");

        H4 userName = new H4(name);
        userName.addClassName("testimonial-name");

        Span userRole = new Span(role);
        userRole.addClassName("testimonial-role");

        Paragraph userQuote = new Paragraph(quote);
        userQuote.addClassName("testimonial-quote");

        card.add(userName, userRole, userQuote);
        return card;
    }
}