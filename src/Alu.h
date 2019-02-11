#pragma once

#include <functional>
#include <unordered_map>

class Memory;

class Alu {
  public:
    Alu(Memory& memory) :
      memory_(memory) {}

  private:
    // RAM Instruction Set


  private:
    // Links between components
    Memory &memory_;
    //
    int ip_{0};

    // Function pointer table
    using InstructionPtr_t = std::function<void(Alu &)>;
    static const std::unordered_map<int, InstructionPtr_t> table_;
};
