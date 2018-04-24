{-# LANGUAGE MagicHash, FlexibleContexts, TypeFamilies, DataKinds, TypeOperators #-}
{-# LANGUAGE AllowAmbiguousTypes #-}
module Qsort where

import Java
import qualified Data.Vector.Generic as G
import qualified Data.Vector  as V
import qualified Data.Vector.Generic.Mutable as M



data {-# CLASS "io.vavr.collection.List" #-} VList a= VList (Object# (VList a))
    deriving Class

data {-# CLASS "java.util.function.Function" #-} Function t r = Function (Object# (Function t r))
  deriving Class

foreign import java unsafe "@wrapper apply"
  mkFunction :: (t <: Object, r <: Object) => (t -> Java (Function t r) r) -> Function t r

data {-# CLASS "java.lang.Runnable" #-}  Runnable = Runnable (Object# Runnable)
  deriving Class

foreign import java unsafe "@wrapper run"
  runnable :: Java Runnable () -> Runnable


foreign import java unsafe "head" vhead ::
  (a <: Object, b <: (VList a)) => Java b a


foreign import java unsafe "tail" vtail ::
    (a <: Object, b <: (VList a)) => Java b (VList a)


foreign import java unsafe "isEmpty" visEmpty ::
   (a <: Object, b <: (VList a)) =>  Java b Bool


foreign export java "@static eta.example.MyExportedClass.sort"
   sort :: JIntArray -> JIntArray

foreign export java "@static eta.example.MyExportedClass.sortv"
   sortv :: JIntArray -> JIntArray


from:: JIntArray -> [Int]
from li = fromJava li



sort  li = toJava ( quicksort ( from li ) )


sortv li = toJava ( myvsort ( from li))

quicksort [] = []
quicksort (x:xs) = quicksort left ++ [x] ++ quicksort right
    where
          left  = [ y | y <- xs, y < x ]
          right = [ y | y <- xs, y >= x ]



mklist ::  VList JInteger -> IO [Int]
mklist li =  do
        empty <-  javaWith li visEmpty
        if  empty
        then  return []
        else     (javaWith li vhead ) >>=(\x -> return [fromJava x])


qvsort :: (G.Vector v a, Ord a) => v a -> v a
qvsort = G.modify go where
    go xs | M.length xs < 2 = return ()
          | otherwise = do
            p <- M.read xs (M.length xs `div` 2)
            j <- M.unstablePartition (< p) xs
            let (l, pr) = M.splitAt j xs
            k <- M.unstablePartition (== p) pr
            go l; go $ M.drop k pr

myvsort ::[Int] ->[Int]
myvsort li =
    let vec = V.fromList li :: (V.Vector Int)
        sorted = qvsort vec :: (V.Vector Int)
        converted = V.toList sorted :: [Int]
    in converted