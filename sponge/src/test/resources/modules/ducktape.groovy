import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameInitializationEvent
import net.year4000.utilities.ducktape.module.Module
import org.spongepowered.api.event.message.MessageChannelEvent

@Module(id = 'ducktape')
class DucktapeModule {
    DucktapeModule() {
        println 'I have been loaded yea!!!'
    }

    @Listener
    void onInit(GameInitializationEvent event) {
        println 'I ran the GameInitializationEvent listener'
    }

    @Listener
    void onChat(MessageChannelEvent.Chat event) {
        println 'I spoke in the chat'
    }
}
