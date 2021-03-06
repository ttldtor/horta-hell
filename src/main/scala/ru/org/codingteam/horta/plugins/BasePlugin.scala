package ru.org.codingteam.horta.plugins

import akka.actor.{Actor, ActorLogging}
import ru.org.codingteam.horta.database.DAO

/**
 * CommandPlugin class used as base for all command plugins.
 */
abstract class BasePlugin extends Actor with ActorLogging {
  def receive = {
    case GetPluginDefinition => sender ! pluginDefinition
  }

  protected val core = context.actorSelection("/user/core")
  protected val store = context.actorSelection("/user/core/store")

  /**
   * Plugin name.
   * @return unique plugin name.
   */
  protected def name: String

  /**
   * Plugin notification sources.
   * @return object containing definition of notification sources.
   */
  protected def notifications = Notifications(messages = false, rooms = false, participants = false)

  /**
   * A collection of command definitions.
   * @return a collection.
   */
  protected def commands: List[CommandDefinition] = List()

  /**
   * Plugin data access object. May be None if not present.
   * @return data access object.
   */
  protected def dao: Option[DAO] = None

  /**
   * A full plugin definition.
   * @return plugin definition.
   */
  private def pluginDefinition = PluginDefinition(name, notifications, commands, dao)
}
