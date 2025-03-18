package github.tyonakaisan.maturi.game.syateki.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.UpdatingInterface;
import org.incendo.interfaces.core.arguments.HashMapInterfaceArguments;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.core.transform.InterfaceProperty;
import org.incendo.interfaces.core.transform.Transform;
import org.incendo.interfaces.core.transform.TransformContext;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.click.InventoryClickContext;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.ChestInterface;
import org.incendo.interfaces.paper.type.ChildTitledInterface;
import org.incendo.interfaces.paper.type.Clickable;
import org.incendo.interfaces.paper.type.CloseHandler;
import org.incendo.interfaces.paper.view.ChestView;
import org.incendo.interfaces.paper.view.PlayerView;

import java.util.ArrayList;
import java.util.List;

// ChestInterface.Builder#addTransform()がfor文で機能しないため
// 使ってないから一時的に直す

/**
 * Functions exactly the same as {@link ChestInterface}.
 */
@DefaultQualifier(NonNull.class)
public final class ReChestInterface implements ChildTitledInterface<ChestPane, PlayerViewer>, UpdatingInterface, Clickable<ChestPane, InventoryClickEvent, PlayerViewer> {

    private final int rows;
    private final @NonNull List<TransformContext<ChestPane, PlayerViewer>> transformationList;
    private final @NonNull List<CloseHandler<ChestPane>> closeHandlerList;
    private final @NonNull Component title;
    private final boolean updates;
    private final int updateDelay;
    private final @NonNull ClickHandler<ChestPane, InventoryClickEvent, PlayerViewer, InventoryClickContext<ChestPane,
            ChestView>> clickHandler;

    /**
     * Constructs {@code ChestInterface}.
     *
     * @param rows          the rows
     * @param title         the interfaces title
     * @param transforms    the transformations to apply
     * @param closeHandlers the close handlers to apply
     * @param updates       {@code true} if the interface is an updating interface
     * @param updateDelay   the update delay
     * @param clickHandler  the handler to run on click
     */
    public ReChestInterface(
            final int rows,
            final @NonNull Component title,
            final @NonNull List<TransformContext<ChestPane, PlayerViewer>> transforms,
            final @NonNull List<CloseHandler<ChestPane>> closeHandlers,
            final boolean updates,
            final int updateDelay,
            final @NonNull ClickHandler<ChestPane, InventoryClickEvent, PlayerViewer, InventoryClickContext<ChestPane,
                    ChestView>> clickHandler
    ) {
        this.title = title;
        this.transformationList = transforms;
        this.closeHandlerList = closeHandlers;
        this.updates = updates;
        this.updateDelay = updateDelay;
        this.rows = rows;
        this.clickHandler = clickHandler;
    }

    /**
     * Returns a new ChestInterface builder.
     *
     * @return the builder
     */
    public static @NonNull Builder builder() {
        return new Builder();
    }

    /**
     * Returns the amount of rows.
     *
     * @return the rows
     */
    public int rows() {
        return this.rows;
    }

    @Override
    public @NonNull ClickHandler<ChestPane, InventoryClickEvent, PlayerViewer,
            InventoryClickContext<ChestPane, ChestView>> clickHandler() {
        return this.clickHandler;
    }

    @Override
    public @NonNull ReChestInterface transform(final @NonNull Transform<ChestPane, PlayerViewer> transform) {
        this.transformationList.add(
                TransformContext.of(
                        1,
                        transform
                )
        );
        return this;
    }

    @Override
    public @NonNull List<TransformContext<ChestPane, PlayerViewer>> transformations() {
        return List.copyOf(this.transformationList);
    }

    /**
     * Returns the list of close handlers.
     *
     * @return the close handlers
     */
    public @NonNull List<CloseHandler<ChestPane>> closeHandlers() {
        return List.copyOf(this.closeHandlerList);
    }

    @Override
    public @NonNull ChestView open(final @NonNull PlayerViewer viewer) {
        return this.open(viewer, HashMapInterfaceArguments.empty());
    }

    @Override
    public @NonNull ChestView open(
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArguments arguments
    ) {
        return this.open(viewer, arguments, this.title);
    }

    @Override
    public @NonNull ChestView open(
            final @NonNull PlayerViewer viewer,
            final @NonNull Component title
    ) {
        return this.open(viewer, HashMapInterfaceArguments.empty(), title);
    }

    @Override
    public @NonNull ChestView open(
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArguments arguments,
            final @NonNull Component title
    ) {
        final var chestInterface = new ChestInterface(this.rows, this.title, this.transformationList, this.closeHandlerList, this.updates, this.updateDelay, false, this.clickHandler);
        final @NonNull ChestView view = new ChestView(chestInterface, viewer, arguments, title);

        view.open();

        return view;
    }

    @Override
    public @NonNull InterfaceView<ChestPane, PlayerViewer> open(
            @NonNull final InterfaceView<?, PlayerViewer> parent,
            @NonNull final InterfaceArguments arguments,
            @NonNull final Component title
    ) {
        final var chestInterface = new ChestInterface(this.rows, this.title, this.transformationList, this.closeHandlerList, this.updates, this.updateDelay, false, this.clickHandler);
        final @NonNull ChestView view = new ChestView((PlayerView<?>) parent, chestInterface, parent.viewer(), arguments, title);

        view.open();

        return view;
    }

    @Override
    public @NonNull ChestView open(
            final @NonNull InterfaceView<?, PlayerViewer> parent,
            final @NonNull InterfaceArguments arguments
    ) {
        final var chestInterface = new ChestInterface(this.rows, this.title, this.transformationList, this.closeHandlerList, this.updates, this.updateDelay, false, this.clickHandler);
        final @NonNull ChestView view = new ChestView((PlayerView<?>) parent, chestInterface, parent.viewer(), arguments, this.title);

        view.open();

        return view;
    }


    /**
     * Sets the title of the interface.
     *
     * @return the title
     */
    @Override
    public @NonNull Component title() {
        return this.title;
    }

    /**
     * Returns true if updating interface, false if not.
     *
     * @return true if updating interface, false if not
     */
    @Override
    public boolean updates() {
        return this.updates;
    }

    /**
     * Returns the update delay.
     *
     * @return the update delay
     */
    @Override
    public int updateDelay() {
        return this.updateDelay;
    }

    /**
     * A class that builds a chest interface.
     */
    public static final class Builder implements Interface.Builder<ChestPane, PlayerViewer, org.incendo.interfaces.paper.type.ChestInterface> {

        /**
         * The list of transformations.
         */
        private final @NonNull List<@NonNull TransformContext<ChestPane, PlayerViewer>> transformsList;

        /**
         * The list of close handlers.
         */
        private final @NonNull List<@NonNull CloseHandler<ChestPane>> closeHandlerList;

        /**
         * The amount of rows.
         */
        private int rows;

        /**
         * The title.
         */
        private @NonNull Component title;

        /**
         * True if updating interface, false if not.
         */
        private boolean updates;

        /**
         * How many ticks to wait between interface updates.
         */
        private int updateDelay;

        /**
         * The top click handler.
         */
        private @NonNull ClickHandler<ChestPane, InventoryClickEvent, PlayerViewer, InventoryClickContext<ChestPane, ChestView>> clickHandler;

        /**
         * Constructs {@code Builder}.
         */
        public Builder() {
            this.transformsList = new ArrayList<>();
            this.closeHandlerList = new ArrayList<>();
            this.rows = 1;
            this.title = Component.empty();
            this.updates = false;
            this.updateDelay = 1;
            this.clickHandler = ClickHandler.cancel();
        }

        /**
         * Returns the number of rows for the interface.
         *
         * @return the number of rows
         */
        public int rows() {
            return this.rows;
        }

        /**
         * Sets the number of rows for the interface.
         *
         * @param rows the number of rows
         * @return new builder instance
         */
        public @NonNull Builder rows(final int rows) {
            this.rows = rows;
            return this;
        }

        /**
         * Returns the title of the interface.
         *
         * @return the title
         */
        public @NonNull Component title() {
            return this.title;
        }

        /**
         * Sets the title of the interface.
         *
         * @param title the title
         * @return new builder instance
         */
        public @NonNull Builder title(final @NonNull Component title) {
            this.title = title;
            return this;
        }

        /**
         * Adds a close handler to the interface.
         *
         * @param closeHandler the close handler
         * @return new builder instance.
         */
        public @NonNull Builder addCloseHandler(final @NonNull CloseHandler<ChestPane> closeHandler) {
            this.closeHandlerList.add(closeHandler);

            return this;
        }

        /**
         * Adds a transformation to the interface.
         *
         * @param transform the transformation
         * @return new builder instance.
         */
        @Override
        public @NonNull Builder addTransform(
                final int priority,
                final @NonNull Transform<ChestPane, PlayerViewer> transform,
                final @NonNull InterfaceProperty<?>... property
        ) {
            this.transformsList.add(TransformContext.of(
                    priority,
                    transform,
                    property
            ));

            return this;
        }

        /**
         * Adds a transformation to the interface.
         *
         * @param transform the transformation
         * @return new builder instance.
         */
        @Override
        public @NonNull Builder addTransform(final @NonNull Transform<ChestPane, PlayerViewer> transform) {
            return this.addTransform(1, transform, InterfaceProperty.dummy());
        }

        /**
         * Returns the click handler.
         *
         * @return click handler
         */
        public @NonNull ClickHandler<ChestPane, InventoryClickEvent, PlayerViewer, InventoryClickContext<ChestPane, ChestView>> clickHandler() {
            return this.clickHandler;
        }

        /**
         * Sets the click handler.
         *
         * @param handler the handler
         * @return new builder instance
         */
        public @NonNull Builder clickHandler(final @NonNull ClickHandler<ChestPane, InventoryClickEvent, PlayerViewer, InventoryClickContext<ChestPane, ChestView>> handler) {
            this.clickHandler = handler;
            return this;
        }

        /**
         * Controls how/if the interface updates.
         *
         * @param updates     true if the interface should update, false if not
         * @param updateDelay how many ticks to wait between updates
         * @return new builder instance
         */
        public @NonNull Builder updates(final boolean updates, final int updateDelay) {
            this.updates = updates;
            this.updateDelay = updateDelay;
            return this;
        }

        /**
         * Constructs and returns the interface.
         *
         * @return the interface
         */
        @Override
        public @NonNull ChestInterface build() {
            return new ChestInterface(
                    this.rows,
                    this.title,
                    this.transformsList,
                    this.closeHandlerList,
                    this.updates,
                    this.updateDelay,
                    false,
                    this.clickHandler
            );
        }

    }

}
