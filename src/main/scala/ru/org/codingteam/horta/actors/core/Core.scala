package ru.org.codingteam.horta.actors.core

import akka.actor.{ActorRef, Props, ActorLogging, Actor}
import ru.org.codingteam.horta.messages._
import ru.org.codingteam.horta.actors.Messenger
import ru.org.codingteam.horta.security._
import scala.Some

class Core extends Actor with ActorLogging {
  var commands = Map[String, Command]()
  var plugins: Map[String, ActorRef] = null

  override def preStart() = {
    val messenger = context.actorOf(Props(new Messenger(self)), "messenger")
    plugins = Map("messenger" -> messenger)
  }

  def receive = {
    case RegisterCommand(command, role, receiver) => {
      commands = commands.updated(command, new Command(command, role, receiver))
    }

    case ProcessCommand(user, message) => {
      val arguments = parseCommand(message)
      arguments match {
        case Some(CommandArguments(Command(name, role, target), args)) => {
          if (accessGranted(user, role)) {
            target ! ExecuteCommand(user, name, args)
          }
        }

        case None =>
      }
    }
  }

  def accessGranted(user: User, role: UserRole) = {
    role match {
      case BotOwner()    => user.role == BotOwner()
      case KnownUser()   => user.role == BotOwner() || user.role == KnownUser()
      case UnknownUser() => user.role == BotOwner() || user.role == KnownUser() || user.role == UnknownUser()
    }
  }

  val commandNameRegex = "^\\$([^\\s]+).*?$".r

  def parseCommand(message: String) = {
    message match {
      case commandNameRegex(command) => {
        commands.get(command) match {
          case Some(command) => Some(CommandArguments(command, parseArguments(message)))
          case None          => None
        }
      }
      case _ => None
    }
  }

  def parseArguments(message: String) = {
    // TODO: Advanced parser with quote syntax
    message.split(' ').tail
  }
}