package redis4s

import cats.free._
import redis4s.CommandCodec.Aux
import redis4s.ops._

package object free {
  type RedisIO[A] = FreeApplicative[RequestOp, A]

  object RedisIO {
    def lift[R, P](command: R)(implicit codec: Aux[R, P]): RedisIO[P] =
      FreeApplicative.lift(RequestOp.Req(command, codec))

    def pure[A](a: A): RedisIO[A] = FreeApplicative.pure(a)

    val client: RedisClient[RedisIO] = new RedisClient[RedisIO]
      with StringOps[RedisIO]
      with KeyOps[RedisIO]
      with StreamOps[RedisIO]
      with ConnectionOps[RedisIO]
      with ServerOps[RedisIO]
      with RunOps[RedisIO] {
      override def run[R, P](r: R)(implicit codec: Aux[R, P]): RedisIO[P] = RedisIO.lift(r)(codec)
    }
  }
}
