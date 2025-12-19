package dev.doctor4t.wathe.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.concurrent.CopyOnWriteArrayList;

public class Scheduler {
    public static class ScheduledTask {
        private int ticksLeft;
        private final Runnable action;
        private boolean cancelled = false;

        public ScheduledTask(int delayTicks, Runnable action) {
            this.ticksLeft = delayTicks;
            this.action = action;
        }

        public boolean tick() {
            if (cancelled) return true;
            if (--ticksLeft <= 0) {
                action.run();
                return true;
            }
            return false;
        }

        public void cancel() {
            this.cancelled = true;
        }
    }

    private static final CopyOnWriteArrayList<ScheduledTask> TASKS = new CopyOnWriteArrayList<>();

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> TASKS.removeIf(ScheduledTask::tick));
    }

    public static ScheduledTask schedule(Runnable action, int delayTicks) {
        ScheduledTask task = new ScheduledTask(delayTicks, action);
        TASKS.add(task);
        return task;
    }
}
